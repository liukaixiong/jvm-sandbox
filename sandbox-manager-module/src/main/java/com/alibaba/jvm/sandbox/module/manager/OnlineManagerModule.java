package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.ModuleManager;
import com.alibaba.jvm.sandbox.module.manager.components.AbstractCommandInvoke;
import com.alibaba.jvm.sandbox.module.manager.components.GroupContainerHelper;
import com.alibaba.jvm.sandbox.module.manager.handler.HeartbeatHandler;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 在线模块
 *
 * @author liukaixiong
 */
@MetaInfServices(Module.class)
@Information(id = Constants.DEFAULT_MODULE_ID, version = "0.0.1", author = "liukaixiong")
public class OnlineManagerModule implements Module, LoadCompleted {

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

        scanPackage("com.alibaba.jvm.sandbox.module.manager.process");
    }

    public void scanPackage(String packages) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter((MetadataReader mr, MetadataReaderFactory mrf) -> {
            return AbstractCommandInvoke.class.getName().equals(mr.getClassMetadata().getSuperClassName());
        });
        Set<BeanDefinition> candidateComponents = provider.findCandidateComponents(packages);
        candidateComponents.forEach((beanDefinition) -> {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                AbstractCommandInvoke abstractCommandInvoke = (AbstractCommandInvoke) clazz.newInstance();
                CommandEnums commandEnums = abstractCommandInvoke.commandName();
                GroupContainerHelper.getInstance().registerCommandInvoke(commandEnums, clazz);
            } catch (Exception e) {
                // 忽略
                e.printStackTrace();
            }
        });
    }


}
