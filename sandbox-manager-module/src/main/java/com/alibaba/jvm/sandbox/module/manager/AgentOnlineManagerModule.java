package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.ModuleManager;
import com.alibaba.jvm.sandbox.module.manager.components.AbstractCommandInvoke;
import com.alibaba.jvm.sandbox.module.manager.components.GroupContainerHelper;
import com.alibaba.jvm.sandbox.module.manager.debug.CommandDebugProcess;
import com.alibaba.jvm.sandbox.module.manager.handler.HeartbeatHandler;
import com.alibaba.jvm.sandbox.module.manager.process.callback.CommandPostCallback;
import com.alibaba.jvm.sandbox.module.manager.process.callback.HttpCommandLogSendCallback;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;
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
 *
 * @author liukaixiong
 */
@MetaInfServices(Module.class)
@Information(id = Constants.DEFAULT_MODULE_ID, version = "0.0.1", author = "liukaixiong", mode = Information.Mode.AGENT)
public class AgentOnlineManagerModule implements Module, LoadCompleted {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private ConfigInfo configInfo;

    @Resource
    private ModuleManager moduleManager;


    private HeartbeatHandler heartbeatHandler;

    @Override
    public void loadCompleted() {
        // 开启心跳线程
        heartbeatHandler = new HeartbeatHandler(configInfo, moduleManager);
        heartbeatHandler.start();

        // 注册watcher执行器
        scanWatcherCommandPackage("com.alibaba.jvm.sandbox.module.manager.process");
        // 注册debug执行器
        scanDebugCommandPackage("com.alibaba.jvm.sandbox.module.manager.debug");

        // 注册回调接口
        GroupContainerHelper.getInstance().registerList(CommandPostCallback.class, new HttpCommandLogSendCallback());

    }

    private void scanDebugCommandPackage(String packages) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(CommandDebugProcess.class));
        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(packages);
        candidateComponents.forEach((beanDefinition) -> {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                CommandDebugProcess debugProcess = (CommandDebugProcess) clazz.newInstance();
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
                Constructor constructor = clazz.getDeclaredConstructor(CommandWatcherInfoModel.class);
                AbstractCommandInvoke abstractCommandInvoke = (AbstractCommandInvoke) constructor.newInstance(new Object[]{new CommandWatcherInfoModel()});
                CommandEnums.Watcher commandEnums = abstractCommandInvoke.commandName();
                GroupContainerHelper.getInstance().registerWatcherCommandInvoke(commandEnums, clazz);
            } catch (Exception e) {
                // 忽略
                e.printStackTrace();
            }
        });
    }


}
