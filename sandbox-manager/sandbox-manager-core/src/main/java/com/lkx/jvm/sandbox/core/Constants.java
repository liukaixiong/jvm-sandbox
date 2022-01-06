package com.lkx.jvm.sandbox.core;

/**
 * 固定的常量定义
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/8 - 11:18
 */
public class Constants {
    /**
     *
     */
    public final static String DEFAULT_CONFIG_DIR = "/cfg";

    public final static String DEFAULT_PLUGINS_DIR = "/plugins";

    public final static String CONFIG_NAME = "/web-manager.properties";

    public final static String DEFAULT_CONFIG_WEB_CONSOLE_VALUE = "http://127.0.0.1:8010";

    public final static String DEFAULT_CONFIG_WEB_HEARTBEAT_VALUE = "/heartbeat/push";
    public final static String DEFAULT_CONFIG_WEB_COMMAND_CONFIG_VALUE = "/config/command";
    public final static String DEFAULT_CONFIG_WEB_COMMAND_PUSH_LOG_VALUE = "/config/command/push/log";

    public final static String SANDBOX_VERSION = "1.3.3";

    public final static String DEFAULT_MODULE_ID = "online-manager-module";
    public final static String SPRING_BEAN_MODULE_ID = "spring-bean-module";
    public final static String PLUGIN_MANAGER_MODULE_ID = "plugin-manager-module";


    /**
     * 控制台地址
     */
    public final static String WEB_CONSOLE_URL_NAME = "web.console.url";

    /**
     * 心跳发送地址
     */
    public final static String WEB_CONSOLE_HEARTBEAT_NAME = "web.heartbeat.path";
    public final static String WEB_CONSOLE_COMMAND_CONFIG_NAME = "web.config.command.path";
    public final static String WEB_CONSOLE_COMMAND_CONFIG_LOG_PUSH_NAME = "web.config.command.log.push";

    /**
     * 沙箱的暴露http接口路径
     */
    public final static String DEFAULT_SANDBOX_PATH = "/sandbox/default/module/http/";
    public final static String MODULE_COMMAND_DEBUG_HTTP_ID = "command-debug-http";
    public final static String MODULE_COMMAND_DEBUG_HTTP_INVOKE = "command-debug-http-invoke";


    public final static String MODULE_COMMAND_WATCHER_HTTP_ID = "command-watcher-http";
    public final static String MODULE_COMMAND_HTTP_LIST = "command-list";
    public final static String MODULE_COMMAND_HTTP_REGISTER = "command-register";
    public final static String MODULE_COMMAND_HTTP_STOP = "command-stop";

    public static final String MODULE_PLUGIN_HTTP_LIST = "plugin-manager-list";
    public static final String MODULE_PLUGIN_HTTP_REGISTER= "plugin-manager-register";
    public static final String MODULE_PLUGIN_HTTP_STOP= "plugin-manager-stop";

}
