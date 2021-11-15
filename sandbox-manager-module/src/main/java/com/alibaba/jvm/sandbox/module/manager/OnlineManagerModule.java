package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.ModuleManager;
import com.alibaba.jvm.sandbox.module.manager.handler.HeartbeatHandler;
import com.lkx.jvm.sandbox.core.Constants;
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
public class OnlineManagerModule  implements Module, LoadCompleted {

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
    }


}
