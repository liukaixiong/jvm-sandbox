package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.ModuleController;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleManager;
import com.alibaba.jvm.sandbox.module.manager.components.AbstractCommandInvoke;
import com.alibaba.jvm.sandbox.module.manager.components.CommandContainerHelper;
import com.alibaba.jvm.sandbox.module.manager.consts.ManagerConstants;
import com.alibaba.jvm.sandbox.module.manager.plugin.EventWatcherLifeCycle;
import com.alibaba.jvm.sandbox.module.manager.plugin.PluginManager;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.factory.GlobalFactoryHelper;
import com.lkx.jvm.sandbox.core.factory.GlobalInstanceFactory;
import com.lkx.jvm.sandbox.core.factory.TypeFactoryService;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;
import com.sandbox.manager.api.SpringLoadCompleted;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.wiring.BeanConfigurerSupport;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

/**
 * 在线模块
 * <p>
 * 一定要确保这个类是最先开始加载的，否则和这个模块相关的插件会有问题。
 *
 * @author liukaixiong
 */
@MetaInfServices(Module.class)
@Information(id = Constants.DEFAULT_MODULE_ID, version = "0.0.1", author = "liukaixiong", mode = Information.Mode.AGENT)
public class AgentOnlineManagerModule implements Module, LoadCompleted, SpringLoadCompleted {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private ConfigInfo configInfo;

    @Resource
    private ModuleManager moduleManager;

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Resource
    private ModuleController moduleController;

    private AnnotationConfigApplicationContext annotationConfigApplicationContext;

    public static final String PROCESSOR_PACKAGE = "com.alibaba.jvm.sandbox.module.manager.process";

    @Override
    public void loadCompleted() {
        try {
            refreshManagerFactory();
        } catch (Exception e) {
            log.error("manager initialization error : ", e);
        }
    }

    public AnnotationConfigApplicationContext getAnnotationConfigApplicationContext() {
        return annotationConfigApplicationContext;
    }

    /**
     * 刷新管理器工厂
     *
     * @return
     * @throws Exception
     */
    public void refreshManagerFactory() throws Exception {
        String pluginDirName = ManagerConstants.PLUGIN_DIR_NAME;
        String pluginPath = configInfo.getUserModuleLibPaths()[0] + "\\" + pluginDirName;

        // todo 得修改
        // String pluginPath = "E:\\study\\sandbox\\sandbox-module\\manager-plugins";

        // 构建工厂实例
        this.annotationConfigApplicationContext = new AnnotationConfigApplicationContext();

        registerSpringInternalClass(this.annotationConfigApplicationContext);

        // 基于Spring构建一套容器工厂
        annotationConfigApplicationContext.scan("com.alibaba.jvm.sandbox.module.manager");

        annotationConfigApplicationContext.refresh();

        Map<String, TypeFactoryService> typeFactoryService = annotationConfigApplicationContext.getBeansOfType(TypeFactoryService.class);

        // 注册全局工厂
        GlobalInstanceFactory globalInstanceFactory = new GlobalInstanceFactory();
        globalInstanceFactory.registerFactory(typeFactoryService.values().toArray(new TypeFactoryService[0]));

        // 初始化默认的插件模块
        PluginManager pluginManager = annotationConfigApplicationContext.getBean(PluginManager.class);

        // 初始化默认的插件 todo -> 从数据库中查找该应用的插件信息列表并加载.
        pluginManager.loadPlugin(pluginPath, null, new EventWatcherLifeCycle(this.moduleEventWatcher));

        //注册关键的四大对象
        GlobalFactoryHelper.core().registerObject(ConfigInfo.class, configInfo);
        GlobalFactoryHelper.core().registerObject(ModuleManager.class, moduleManager);
        GlobalFactoryHelper.core().registerObject(ModuleEventWatcher.class, moduleEventWatcher);
        GlobalFactoryHelper.core().registerObject(ModuleController.class, moduleController);

        scanWatcherCommandPackage();
    }

    /**
     * 注册Spring内部对象
     *
     * @param annotationConfigApplicationContext
     */
    private void registerSpringInternalClass(AnnotationConfigApplicationContext annotationConfigApplicationContext) {
        annotationConfigApplicationContext.register(BeanConfigurerSupport.class);
    }

    public void scanWatcherCommandPackage() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter((MetadataReader mr, MetadataReaderFactory mrf) -> AbstractCommandInvoke.class.getName().equals(mr.getClassMetadata().getSuperClassName()));
        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(PROCESSOR_PACKAGE);
        candidateComponents.forEach((beanDefinition) -> {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                Constructor<?> constructor = clazz.getDeclaredConstructor(CommandWatcherInfoModel.class);
                AbstractCommandInvoke abstractCommandInvoke = (AbstractCommandInvoke) constructor.newInstance(new Object[]{new CommandWatcherInfoModel()});
                CommandEnums.Watcher commandEnums = abstractCommandInvoke.commandName();
                CommandContainerHelper.getInstance().registerWatcherCommandInvoke(commandEnums, clazz);
            } catch (Exception e) {
                // 忽略
                e.printStackTrace();
            }
        });
    }


}
