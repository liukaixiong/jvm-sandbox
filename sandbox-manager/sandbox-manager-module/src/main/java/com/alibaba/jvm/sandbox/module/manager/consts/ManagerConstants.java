package com.alibaba.jvm.sandbox.module.manager.consts;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/17 - 10:57
 */
public class ManagerConstants {

    /**
     * 插件包的包名，这里为了避免扫描到每个插件包所有资源，事先约定好包的前缀名称，减少资源的消耗
     * 带来的问题就是你如果需要写一个插件的话，包名必须以这个前缀为主。
     */
    public static final String PLUGIN_PACKAGE = "com.sandbox.application.plugin";
    /**
     * 插件目录名称
     */
    public static final String PLUGIN_DIR_NAME = "manager-plugins";

    /**
     * 插件的应用配置名称
     */
    public static final String PLUGIN_PROPERTIES_NAME = "application.properties";

    /**
     * 主应用的配置前缀标识
     */
    public static final String PROPERTIES_STARTS_WITH = "spring.sandbox.plugin.register";

}
