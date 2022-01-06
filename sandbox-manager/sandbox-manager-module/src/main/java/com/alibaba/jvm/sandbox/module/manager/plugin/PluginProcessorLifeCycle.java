package com.alibaba.jvm.sandbox.module.manager.plugin;

import com.sandbox.manager.api.processors.AbstractPluginModuleDefinitionProcessor;

import java.util.List;

/**
 * 插件的生命周期
 * <p>
 * 希望在构建和卸载的时候能有一个清晰的边界划分，能把这两部分很好的管理起来
 *
 * @author liukaixiong
 * @date 2021/12/30 - 14:27
 */
public interface PluginProcessorLifeCycle {

    /**
     * 初始化插件执行
     *
     * @param pluginModuleDefinitionProcessor 插件执行集合
     */
    void initialization(List<AbstractPluginModuleDefinitionProcessor> pluginModuleDefinitionProcessor);

    /**
     * 销毁插件
     */
    void destroy();

}
