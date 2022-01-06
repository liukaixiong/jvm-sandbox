package com.alibaba.jvm.sandbox.module.manager.model;

import java.util.List;

/**
 * 插件集合模型
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/4 - 16:22
 */
public class PluginListModel {

    private List<String> loadComplete;

    private List<String> unload;

    private List<PluginEventWatcherInfo> pluginEventWatcherInfoList;

    public List<PluginEventWatcherInfo> getPluginEventWatcherInfoList() {
        return pluginEventWatcherInfoList;
    }

    public void setPluginEventWatcherInfoList(List<PluginEventWatcherInfo> pluginEventWatcherInfoList) {
        this.pluginEventWatcherInfoList = pluginEventWatcherInfoList;
    }

    public List<String> getLoadComplete() {
        return loadComplete;
    }

    public void setLoadComplete(List<String> loadComplete) {
        this.loadComplete = loadComplete;
    }

    public List<String> getUnload() {
        return unload;
    }

    public void setUnload(List<String> unload) {
        this.unload = unload;
    }
}
