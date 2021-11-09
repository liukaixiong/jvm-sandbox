package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleManager;
import com.alibaba.jvm.sandbox.module.manager.handler.HeartbeatHandler;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * 在线模块
 *
 * @author liukaixiong
 */
@MetaInfServices(Module.class)
@Information(id = Constants.DEFAULT_MODULE_ID, version = "0.0.1", author = "liukaixiong")
public class OnlineManagerModule implements Module, LoadCompleted {

    private final Logger exLogger = LoggerFactory.getLogger("ONLINE-MODULE-LOGGER");

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Resource
    private ConfigInfo configInfo;

    @Resource
    private ModuleManager moduleManager;

    private HeartbeatHandler heartbeatHandler;

    @Override
    public void loadCompleted() {
//        new EventWatchBuilder(moduleEventWatcher)
//                .onClass(Exception.class)
//                .includeBootstrap()
//                .onBehavior("<init>")
//                .onWatch(new EventListener() {
//                    @Override
//                    public void onEvent(Event event) throws Throwable {
//                        final BeforeEvent bEvent = (BeforeEvent) event;
//                        exLogger.info("{} occur an exception: {}",
//                                getJavaClassName(bEvent.target.getClass()),
//                                bEvent.target
//                        );
//                    }
//                }, BEFORE);

        // 开启心跳线程
        heartbeatHandler = new HeartbeatHandler(configInfo, moduleManager);
        heartbeatHandler.start();
    }

}
