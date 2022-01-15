package com.alibaba.jvm.sandbox.module.manager.handler;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleException;
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.ModuleManager;
import com.alibaba.jvm.sandbox.module.manager.components.SpringApplicationConfig;
import com.alibaba.jvm.sandbox.module.manager.util.PropertyUtil;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.util.HttpUtil;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@link HeartbeatHandler}
 * <p>
 *
 * @author zhaoyb1990
 */
public class HeartbeatHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final long FREQUENCY = 10;

    // 获取心跳发送信息

    private final static String HEARTBEAT_URL = PropertyUtil.getWebHeartbeatPath();

    private static final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("heartbeat-pool-%d").daemon(true).build());

    private final ConfigInfo configInfo;
    private final ModuleManager moduleManager;
    private final AtomicBoolean initialize = new AtomicBoolean(false);

    public HeartbeatHandler(ConfigInfo configInfo, ModuleManager moduleManager) {
        this.configInfo = configInfo;
        this.moduleManager = moduleManager;
    }


    public synchronized void start() {
        // 为每一个应用做一个标记
        if (initialize.compareAndSet(false, true)) {
            logger.info("启动调度程序，负责每{}秒发送一次心跳信息", FREQUENCY);
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info("开始调度程序...");
                        innerReport();
                    } catch (Exception e) {
                        logger.error("error occurred when report heartbeat", e);
                    }
                }
            }, 10, FREQUENCY, TimeUnit.SECONDS);
        }
    }


    public void stop() {
        if (initialize.compareAndSet(true, false)) {
            executorService.shutdown();
        }
    }

    private void innerReport() {
        String runtimeBeanName = ManagementFactory.getRuntimeMXBean().getName();
        String pid = runtimeBeanName.split("@")[0];
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appId", SpringApplicationConfig.getInstance().getAppId());
        params.put("appName", SpringApplicationConfig.getInstance().getApplicationName());
        params.put("ip", SpringApplicationConfig.getInstance().getHost());
        params.put("environment", SpringApplicationConfig.getInstance().getEnvironment());
        params.put("port", configInfo.getServerAddress().getPort() + "");
        params.put("version", configInfo.getVersion());
        params.put("pid", pid);
        // config配置
        params.put("namespace", configInfo.getNamespace());
        params.put("mode", configInfo.getMode().name());
        params.put("isEnableUnsafe", configInfo.isEnableUnsafe() + "");

        // 注册模块
        params.put("moduleList", searchModule());

        try {
            params.put("status", moduleManager.isActivated(Constants.DEFAULT_MODULE_ID) ? "ACTIVE" : "FROZEN");
        } catch (ModuleException e) {
            // ignore
        }
        HttpUtil.invokePostJson(HEARTBEAT_URL, null, params);
    }


    public List<Map<String, Object>> searchModule() {
        List<Map<String, Object>> moduleList = new ArrayList<>();
        for (final Module module : moduleManager.list()) {
            final Information info = module.getClass().getAnnotation(Information.class);
            try {
                Map<String, Object> moduleMap = new LinkedHashMap<>();
                moduleMap.put("id", info.id());
                moduleMap.put("isLoaded", moduleManager.isLoaded(info.id()));
                moduleMap.put("isActivated", moduleManager.isActivated(info.id()));
                moduleMap.put("version", info.version());
                moduleMap.put("author", info.author());
                moduleList.add(moduleMap);
            } catch (ModuleException me) {
                logger.warn("get module info occur error when list modules, module[id={};class={};], error={}, ignore this module.",
                        me.getUniqueId(), module.getClass(), me.getErrorCode(), me);
            }

        }
        return moduleList;
    }

}
