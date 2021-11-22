package com.alibaba.jvm.sandbox.module.manager.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.alibaba.jvm.sandbox.module.manager.util.IPUtils;
import com.alibaba.jvm.sandbox.module.manager.util.PropertyUtil;


/**
 * {@link ApplicationConfig} 描述一个基础应用模型
 * <p>
 * 应用名    {@link ApplicationConfig#appName}
 * 机器名    {@link ApplicationConfig#host}
 * 环境信息  {@link ApplicationConfig#environment}
 * </p>
 *
 * @author zhaoyb1990
 */
public class ApplicationConfig {

    private String appId;

    private String appName;

    private String environment;

    private String host;
    
    private boolean isDisableSubClass = false; 

    private volatile boolean fusing = false;

    private static ApplicationConfig instance = new ApplicationConfig();

    private ApplicationConfig() {
        // for example, you can define it your self
        this.appName = PropertyUtil.getSystemPropertyOrDefault("app.name", "unknown");
        this.environment = PropertyUtil.getSystemPropertyOrDefault("app.env", "unknown");
        try {
            this.host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // default value for disaster
            this.host = "127.0.0.1";
        }
    }

    public static ApplicationConfig instance() {
        return instance;
    }

    /**
     * 是否正在工作（熔断机制）
     *
     * @return true/false
     */
    public boolean isWorkingOn() {
        return !fusing;
    }

    public String getAppName() {
        return appName;
    }


    public String getAppId() {
        if (appId == null) {
            // 并发也没问题，反正幂等
            appId = getAppName() + "-" + IPUtils.genIpHex(host);
        }
        return appId;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isFusing() {
        return fusing;
    }

    public void setFusing(boolean fusing) {
        this.fusing = fusing;
    }


}
