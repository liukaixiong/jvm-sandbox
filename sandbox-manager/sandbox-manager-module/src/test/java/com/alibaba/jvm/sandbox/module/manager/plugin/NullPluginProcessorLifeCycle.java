package com.alibaba.jvm.sandbox.module.manager.plugin;

import com.alibaba.jvm.sandbox.module.manager.model.PluginEventWatcherInfo;
import com.sandbox.manager.api.processors.AbstractPluginModuleDefinitionProcessor;

import java.util.List;

public class NullPluginProcessorLifeCycle implements PluginProcessorWatcherService {

    @Override
    public List<PluginEventWatcherInfo> getPluginEventList() {
        return null;
    }

    @Override
    public void initialization(List<AbstractPluginModuleDefinitionProcessor> pluginModuleDefinitionProcessor) {

    }

    @Override
    public void destroy() {

    }
}