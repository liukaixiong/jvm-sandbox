package com.alibaba.jvm.sandbox.module.manager.plugin;

import com.alibaba.jvm.sandbox.module.manager.model.PluginEventWatcherInfo;

import java.util.List;

/**
 * 插件监听详情
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/5 - 10:55
 */
public interface PluginProcessorWatcherService extends PluginProcessorLifeCycle{

    List<PluginEventWatcherInfo> getPluginEventList();

}
