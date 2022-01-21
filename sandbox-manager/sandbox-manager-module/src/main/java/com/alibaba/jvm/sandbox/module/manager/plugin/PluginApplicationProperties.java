package com.alibaba.jvm.sandbox.module.manager.plugin;

import java.util.Properties;

/**
 * 插件应用属性
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/17 - 11:27
 */
public class PluginApplicationProperties {

    private String name;

    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static PluginApplicationProperties builder(Properties properties) {
        PluginApplicationProperties pluginApplicationProperties = new PluginApplicationProperties();
        if (properties != null) {
            pluginApplicationProperties.setName(properties.getProperty("plugin.application.name", "未知插件"));
            pluginApplicationProperties.setVersion(properties.getProperty("plugin.application.version", "未知版本"));
        }
        return pluginApplicationProperties;
    }
}
