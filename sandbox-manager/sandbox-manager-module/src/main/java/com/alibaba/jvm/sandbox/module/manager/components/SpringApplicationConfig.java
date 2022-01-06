package com.alibaba.jvm.sandbox.module.manager.components;

import com.alibaba.jvm.sandbox.module.manager.model.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spring配置
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 16:20
 */
public class SpringApplicationConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String SPRING_APPLICATIOIN_NAME = "spring.application.name";
    private static final String SPRING_SERVER_PORT = "server.port";

    private SpringApplicationConfig() {
    }

    private static final SpringApplicationConfig INSTANCE = new SpringApplicationConfig();

    public static SpringApplicationConfig getInstance() {
        return INSTANCE;
    }

    public String getApplicationName() {
        return getProperties(SPRING_APPLICATIOIN_NAME, ApplicationConfig.instance().getAppName());
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
        return ApplicationConfig.instance().getHost();
    }

    public Object getEnvironment() {
        return ApplicationConfig.instance().getEnvironment();
    }

    public String getAppId() {
        return ApplicationConfig.instance().getAppId();
    }
}
