package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceAdapterListener;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleManager;
import com.alibaba.jvm.sandbox.module.manager.components.SpringContextContainer;
import com.alibaba.jvm.sandbox.module.manager.consts.ManagerConstants;
import com.alibaba.jvm.sandbox.module.manager.handler.HeartbeatHandler;
import com.alibaba.jvm.sandbox.module.manager.model.EnhanceConfig;
import com.alibaba.jvm.sandbox.module.manager.model.PluginProperties;
import com.alibaba.jvm.sandbox.module.manager.plugin.EventWatcherLifeCycle;
import com.alibaba.jvm.sandbox.module.manager.plugin.PluginApplicationProperties;
import com.alibaba.jvm.sandbox.module.manager.plugin.PluginManager;
import com.alibaba.jvm.sandbox.module.manager.util.ParsePropertiesUtils;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.factory.GlobalFactoryHelper;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.PluginPropertiesRegisterSupport;
import com.sandbox.manager.api.SpringLoadCompleted;
import com.sandbox.manager.api.model.enhance.EnhanceClassInfo;
import com.sandbox.manager.api.processors.AbstractPluginModuleDefinitionProcessor;
import com.sandbox.manager.api.processors.PropertiesPluginProcessor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 获取Spring的上下文内容
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/9 - 16:53
 */
@MetaInfServices(Module.class)
@Information(id = Constants.SPRING_BEAN_MODULE_ID, version = "0.0.1", author = "liukaixiong", mode = Information.Mode.AGENT)
public class SpringBeanModule implements Module, LoadCompleted {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    private ConfigInfo configInfo;

    @Resource
    private ModuleManager moduleManager;

    @Resource
    private ModuleEventWatcher watcher;

    @Override
    public void loadCompleted() {
        new EventWatchBuilder(watcher)
                .onClass("org.springframework.context.event.ContextRefreshedEvent")
                .onBehavior("<init>")
                .onWatch(new AdviceAdapterListener(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        try {
                            Object factory = advice.getParameterArray()[0];
                            SpringContextContainer.getInstance().setApplicationContext(factory);
                            SpringContextContainer.getInstance().setLoad(true);
                            String port = SpringContextContainer.getInstance().getProperties("server.port");
                            String applicationName = SpringContextContainer.getInstance().getApplicationName();
                            HeartbeatHandler heartbeatHandler = new HeartbeatHandler(configInfo, moduleManager);
                            heartbeatHandler.start();
                            log.info("应用: " + applicationName + "  端口 : " + port + " 强制获取Spring的上下文环境并注入成功!");
                            // 这个时候需要回调容器启动方法.
                            springLoadCompletedCallback(SpringLoadCompleted::refreshCallback);

                            /*
                             * 这里可以从应用服务中获取特定的配置，然后根据配置来注册需要的监听。
                             * 需要从各个服务中去定义属性的名称以及监听的事件；
                             * 到达这一步说明已经持有的应用的服务配置，根据配置来灵活注册监听。
                             *
                             * 1. 回调的接口
                             * 2. 配置的结构
                             * 3. 需要支持前端传参的注册，同时应用配置的也算。
                             *
                             */
                            refreshCallback();
                        } catch (Exception e) {
                            springLoadCompletedCallback(SpringLoadCompleted::errorCallback);
                            log.error("[Error-2000]-register spring bean occurred error.", e);
                        } finally {
                            springLoadCompletedCallback(SpringLoadCompleted::callback);
                        }
                    }
                }), Event.Type.BEFORE, Event.Type.RETURN);
    }

    private void springLoadCompletedCallback(Consumer<SpringLoadCompleted> consumer) {
        List<SpringLoadCompleted> springLoadCompletedList = GlobalFactoryHelper.plugin().getList(SpringLoadCompleted.class);
        if (!CollectionUtils.isEmpty(springLoadCompletedList)) {
            for (SpringLoadCompleted springLoadCompleted : springLoadCompletedList) {
                consumer.accept(springLoadCompleted);
            }
        }
    }

    public void refreshCallback() {
        List<PluginPropertiesRegisterSupport> pluginPropertiesRegisterSupports = GlobalFactoryHelper.plugin().getList(PluginPropertiesRegisterSupport.class);

        if (CollectionUtils.isEmpty(pluginPropertiesRegisterSupports)) {
            log.debug("无需增强应用配置");
            return;
        }

        PluginManager pluginManager = GlobalFactoryHelper.getInstance().getObject(PluginManager.class);
        pluginPropertiesRegisterSupports.stream().map(PluginPropertiesRegisterSupport::key).forEach(key -> {
            try {
                PluginProperties pluginProperties = ObjectUtils.defaultIfNull(ParsePropertiesUtils.parseObject(ManagerConstants.PROPERTIES_STARTS_WITH.concat("." + key), null, (pKey) -> {
                    try {
                        return SpringContextContainer.getInstance().getProperties(pKey);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }, PluginProperties.class), new PluginProperties());

                List<EnhanceConfig> enhanceConfigs = pluginProperties.getEnhanceConfigs();
                List<AbstractPluginModuleDefinitionProcessor> pluginModuleDefinitionProcessor = new ArrayList<>();
                for (EnhanceConfig enhanceConfig : enhanceConfigs) {
                    String classPattern = enhanceConfig.getClassPattern();
                    String methodNames = enhanceConfig.getMethodNames();

                    Set<AdviceNameDefinition> adviceNameDefinitions = enhanceConfig.toAdviceNameDefinitions();
                    if (!CollectionUtils.isEmpty(adviceNameDefinitions)) {
                        EnhanceClassInfo enhanceClassInfo = EnhanceClassInfo.builder().classPattern(classPattern).methodPatterns(EnhanceClassInfo.MethodPattern.transform(methodNames)).includeSubClasses(enhanceConfig.isIncludeSubClasses()).build();
                        PropertiesPluginProcessor propertiesPluginProcessor = new PropertiesPluginProcessor(key, Collections.singletonList(enhanceClassInfo), adviceNameDefinitions);
                        pluginModuleDefinitionProcessor.add(propertiesPluginProcessor);
                    }
                }
                String applicationName = SpringContextContainer.getInstance().getApplicationName();
                EventWatcherLifeCycle eventWatcherLifeCycle = new EventWatcherLifeCycle(this.watcher);
                PluginApplicationProperties pluginApplicationProperties = new PluginApplicationProperties();
                pluginApplicationProperties.setName(applicationName);
                pluginApplicationProperties.setVersion("main");
                eventWatcherLifeCycle.initialization(pluginModuleDefinitionProcessor);
                pluginManager.registerPluginInfo(applicationName, pluginApplicationProperties, eventWatcherLifeCycle);
            } catch (Exception e) {
                log.error("刷新应用增强配置异常", e);
            }
        });
    }


}
