package com.alibaba.jvm.sandbox.module.manager.plugin;

import com.sandbox.manager.api.processors.AbstractPluginModuleDefinitionProcessor;

import java.util.List;

/**
 * 空实现
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/30 - 15:49
 */
public class PluginProcessorLifeCycleAdaptor implements PluginProcessorLifeCycle {

    @Override
    public void initialization(List<AbstractPluginModuleDefinitionProcessor> pluginModuleDefinitionProcessor) {
        // 空方法
    }

    @Override
    public void destroy() {
        // 空方法
    }
}
