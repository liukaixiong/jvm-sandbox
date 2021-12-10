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
import com.alibaba.jvm.sandbox.module.manager.debug.CommandDebugProcess;
import com.alibaba.jvm.sandbox.module.manager.handler.HeartbeatHandler;
import com.alibaba.jvm.sandbox.module.manager.process.callback.CommandPostCallback;
import com.alibaba.jvm.sandbox.module.manager.process.callback.HttpCommandLogSendCallback;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.compoents.GroupContainerHelper;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.factory.PluginInstanceFactory;
import com.lkx.jvm.sandbox.core.factory.PluginModuleManager;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;
import com.sandbox.manager.api.SpringLoadCompleted;
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

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ConfigInfo configInfo;

    @Resource
    private ModuleManager moduleManager;

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Resource
    private ModuleController moduleController;

    public static final String PROCESSOR_PACKAGE = "com.alibaba.jvm.sandbox.module.manager.process";

    public static final String DEBUG_PACKAGE = "com.alibaba.jvm.sandbox.module.manager.debug";

    private HeartbeatHandler heartbeatHandler;

    private final String pluginDirName = "manager-plugins";

    @Override
    public void loadCompleted() {
        initialization();
    }


    /**
     * 初始化容器
     */
    public void initialization() {
        String pluginPath = configInfo.getUserModuleLibPaths()[0] + "\\" + pluginDirName;

        // 注入四大对象，不过请考虑刷新的情况。需要同步更新这个容器类
        addGlobalCache(ConfigInfo.class, configInfo);
        addGlobalCache(ModuleManager.class, moduleManager);
        addGlobalCache(ModuleEventWatcher.class, moduleEventWatcher);
        addGlobalCache(ModuleController.class, moduleController);

        // 构建插件的实例工厂
        PluginInstanceFactory pluginInstanceFactory = new PluginInstanceFactory();

        // 并注入到全局容器中
        addGlobalCache(PluginInstanceFactory.class, pluginInstanceFactory);

        // 开始构建插件对象
        PluginModuleManager pluginModuleManager = new PluginModuleManager(pluginInstanceFactory);
        pluginModuleManager.loadModule(pluginPath);

        addGlobalCache(PluginModuleManager.class, pluginModuleManager);

        // 注册watcher执行器
        scanWatcherCommandPackage(PROCESSOR_PACKAGE);
        // 注册debug执行器
        scanDebugCommandPackage(DEBUG_PACKAGE);

        // 注册回调接口
        GroupContainerHelper.getInstance().registerList(CommandPostCallback.class, new HttpCommandLogSendCallback());
    }

    private void addGlobalCache(Class<?> clazz, Object obj) {
        GroupContainerHelper.getInstance().registerObject(clazz, obj);
    }

    @Override
    public void callback() {
        heartbeatHandler = new HeartbeatHandler(configInfo, moduleManager);
        heartbeatHandler.start();
    }

    private void scanDebugCommandPackage(String packages) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(CommandDebugProcess.class));
        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(packages);
        candidateComponents.forEach((beanDefinition) -> {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                CommandDebugProcess<?> debugProcess = (CommandDebugProcess<?>) clazz.newInstance();
                GroupContainerHelper.getInstance().registerObject(debugProcess.command().name(), debugProcess, CommandDebugProcess.class);
            } catch (Exception e) {
                // 忽略
                e.printStackTrace();
            }
        });
    }

    public void scanWatcherCommandPackage(String packages) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter((MetadataReader mr, MetadataReaderFactory mrf) -> {
            return AbstractCommandInvoke.class.getName().equals(mr.getClassMetadata().getSuperClassName());
        });
        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(packages);
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
