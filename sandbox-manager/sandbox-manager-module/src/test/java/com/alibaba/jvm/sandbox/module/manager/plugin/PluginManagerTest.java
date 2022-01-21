package com.alibaba.jvm.sandbox.module.manager.plugin;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginManagerTest {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private PluginManager pluginManager = new PluginManager();
    private String pluginPath = "E:\\study\\sandbox\\sandbox-module\\manager-plugins";

    @Test
    public void testLoadPlugin() {
        logger.info("开始加载目录 : {} 下的所有插件包", pluginPath);
        pluginManager.loadPlugin(pluginPath, null, new NullPluginProcessorLifeCycle());
    }

    public void testRegisterPluginInfo() {
    }

    public void testGetObjectList() {
    }

    public void testUnloadInstance() {
    }

    public void testTestLoadPlugin() {
    }
}