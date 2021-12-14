package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.ModuleController;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleManager;
import com.alibaba.jvm.sandbox.module.manager.components.AbstractCommandInvoke;
import com.alibaba.jvm.sandbox.module.manager.components.CommandContainerHelper;
import com.alibaba.jvm.sandbox.module.manager.debug.CommandDebugProcess;
import com.alibaba.jvm.sandbox.module.manager.handler.HeartbeatHandler;
import com.alibaba.jvm.sandbox.module.manager.process.callback.CommandPostCallback;
import com.alibaba.jvm.sandbox.module.manager.process.callback.HttpCommandLogSendCallback;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.factory.*;
import com.lkx.jvm.sandbox.core.listener.RouterAdviceListener;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.MethodAdviceInvoke;
import com.sandbox.manager.api.SpringLoadCompleted;
import com.sandbox.manager.api.model.enhance.EnhanceClassInfo;
import com.sandbox.manager.api.processors.AbstractPluginModuleDefinitionProcessor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AssignableTypeFilter;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.util.List;
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

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private ConfigInfo configInfo;

    @Resource
    private ModuleManager moduleManager;

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Resource
    private ModuleController moduleController;

    /**
     * 主实例工厂
     */
    private InstanceFactory instanceFactory = new InstanceFactory();

    /**
     * 插件实例工厂
     */
    private PluginInstanceFactory pluginInstanceFactory = new PluginInstanceFactory();

    public static final String PROCESSOR_PACKAGE = "com.alibaba.jvm.sandbox.module.manager.process";

    public static final String DEBUG_PACKAGE = "com.alibaba.jvm.sandbox.module.manager.debug";

    private HeartbeatHandler heartbeatHandler;

    private final String pluginDirName = "manager-plugins";

    @Override
    public void loadCompleted() {
        try {
            initialization();
        } catch (Exception e) {
            log.error("manager initialization error : ", e);
        }
    }


    /**
     * 初始化容器
     */
    public void initialization() throws Exception {
        String pluginPath = configInfo.getUserModuleLibPaths()[0] + "\\" + pluginDirName;

        GlobalInstanceFactory globalInstanceFactory = new GlobalInstanceFactory(instanceFactory, pluginInstanceFactory);

        // 全局应用  后续获取对象可以直接从这个类中获取
        GlobalFactoryHelper.setInstance(globalInstanceFactory);

        // ================================ 主容器工厂构建 =========================================
        // 注入四大对象，不过请考虑刷新的情况。需要同步更新这个容器类
        addGlobalCache(ConfigInfo.class, configInfo);
        addGlobalCache(ModuleManager.class, moduleManager);
        addGlobalCache(ModuleEventWatcher.class, moduleEventWatcher);
        addGlobalCache(ModuleController.class, moduleController);

        // 注册watcher执行器
        scanWatcherCommandPackage();

        // 注册debug执行器
        scanDebugCommandPackage();

        //注册公共的监听模块
        scanCommonModule("com.alibaba.jvm.sandbox.module.manager.listener", MethodAdviceInvoke.class);

        // ================================ 插件容器工厂构建 =========================================

        // 开始构建插件对象
        PluginModuleManager pluginModuleManager = new PluginModuleManager(pluginInstanceFactory);
        pluginModuleManager.loadModule(pluginPath);

        addGlobalCache(PluginModuleManager.class, pluginModuleManager);

        // 容器刷新完成之后 : GroupContainerHelper : 全局容器  PluginInstanceFactory 插件容器

        // 注册回调接口
        registerGlobalList(CommandPostCallback.class, new HttpCommandLogSendCallback());

        // 执行插件注册
        List<AbstractPluginModuleDefinitionProcessor> abstractPluginModuleDefinitionProcessors = pluginInstanceFactory.getPluginInstanceList(AbstractPluginModuleDefinitionProcessor.class);

        for (AbstractPluginModuleDefinitionProcessor abstractPluginModuleDefinitionProcessor : abstractPluginModuleDefinitionProcessors) {
            // 执行默认的插件监听
            registerPluginWatch(abstractPluginModuleDefinitionProcessor);
        }
    }

    private void addGlobalCache(Class<?> clazz, Object obj) {
        instanceFactory.registerObject(clazz, obj);
    }


    private void registerGlobalList(Class<?> clazz, Object obj) {
        instanceFactory.registerList(clazz, obj);
    }

    /**
     * 注册插件观察
     *
     * @param processor
     * @throws Exception
     */
    public void registerPluginWatch(AbstractPluginModuleDefinitionProcessor processor) throws Exception {

        List<EnhanceClassInfo> enhanceClassInfos = processor.getEnhanceClassInfos();
        Set<AdviceNameDefinition> methodAdviceInvoke = processor.getMethodAdviceInvoke();
        String moduleName = processor.moduleName();
        String pluginName = processor.pluginName();

        if (CollectionUtils.isEmpty(enhanceClassInfos)) {
            throw new Exception("enhance models is empty, plugin type is " + processor.pluginName());
        }
        // 开始构建EventWatch对象
        for (EnhanceClassInfo eci : enhanceClassInfos) {
            EventWatchBuilder.IBuildingForBehavior behavior = null;

            EventWatchBuilder.IBuildingForClass builder4Class = new EventWatchBuilder(moduleEventWatcher).onClass(eci.getClassPattern());

            if (eci.isIncludeSubClasses()) {
                builder4Class = builder4Class.includeSubClasses();
            }

            for (EnhanceClassInfo.MethodPattern mp : eci.getMethodPatterns()) {
                behavior = builder4Class.onBehavior(mp.getMethodName());
                if (ArrayUtils.isNotEmpty(mp.getParameterType())) {
                    behavior.withParameterTypes(mp.getParameterType());
                }
                if (ArrayUtils.isNotEmpty(mp.getAnnotationTypes())) {
                    behavior.hasAnnotationTypes(mp.getAnnotationTypes());
                }
            }

            if (behavior != null) {
                int watchId = behavior.onWatch(new RouterAdviceListener(methodAdviceInvoke)).getWatchId();
                log.info("add watcher success,moduleName={},pluginName={},watcherId={}", moduleName, pluginName, watchId);
            }

        }
    }

    @Override
    public void callback() {
        heartbeatHandler = new HeartbeatHandler(configInfo, moduleManager);
        heartbeatHandler.start();
    }

    private void scanCommonModule(String packages, Class<?> clazz) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(clazz));
        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(packages);
        candidateComponents.forEach((beanDefinition) -> {
            try {
                Class<?> beanClazz = Class.forName(beanDefinition.getBeanClassName());
                Object object = beanClazz.newInstance();
                instanceFactory.registerList(clazz, object);
            } catch (Exception e) {
                // 忽略
                e.printStackTrace();
            }
        });
    }

    /**
     * 扫描在线调试模块
     */
    private void scanDebugCommandPackage() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(CommandDebugProcess.class));
        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(DEBUG_PACKAGE);
        candidateComponents.forEach((beanDefinition) -> {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                CommandDebugProcess<?> debugProcess = (CommandDebugProcess<?>) clazz.newInstance();
                instanceFactory.registerObject(debugProcess.command().name(), debugProcess, CommandDebugProcess.class);
            } catch (Exception e) {
                // 忽略
                e.printStackTrace();
            }
        });
    }

    public void scanWatcherCommandPackage() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter((MetadataReader mr, MetadataReaderFactory mrf) -> {
            return AbstractCommandInvoke.class.getName().equals(mr.getClassMetadata().getSuperClassName());
        });
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
