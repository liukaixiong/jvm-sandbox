package com.alibaba.jvm.sandbox.module.manager.components;

import com.alibaba.jvm.sandbox.module.manager.model.ApplicationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 16:20
 */
public class ApplicationConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static String SPRING_APPLICATIOIN_NAME = "spring.application.name";
    private static String SPRING_SERVER_PORT = "server.port";

    private ApplicationConfig() {
    }

    private static ApplicationConfig INSTANCE = new ApplicationConfig();

    public static ApplicationConfig getInstance() {
        return INSTANCE;
    }

    public String getApplicationName() {
        return getProperties(SPRING_APPLICATIOIN_NAME, ApplicationModel.instance().getAppName());
    }

    private String getProperties(String key, String defaultValue) {
        try {
            return SpringContextContainer.getInstance().isLoad() ? SpringContextContainer.getInstance().getProperties(key) : defaultValue;
        } catch (Exception e) {
            logger.error("获取属性失败", e);
        }
        return null;
    }

    public Object getHost() {
        return ApplicationModel.instance().getHost();
    }

    public Object getEnvironment() {
        return ApplicationModel.instance().getEnvironment();
    }

    public String getAppId() {
        return ApplicationModel.instance().getAppId();
    }
}
