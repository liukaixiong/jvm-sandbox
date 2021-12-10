package com.lkx.jvm.sandbox.core.factory;

import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;

import java.util.List;

/**
 * 插件模块管理器
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/9 - 14:16
 */
public class PluginModuleManager {

    private PluginInstanceFactory pluginInstanceFactory ;

    public PluginModuleManager(PluginInstanceFactory pluginInstanceFactory) {
        this.pluginInstanceFactory = pluginInstanceFactory;
    }

    public void loadModule(String moduleJarFilePath) {
        pluginInstanceFactory.loadInstance(moduleJarFilePath);
    }

    public void unloadModule(String jarFile) {
        pluginInstanceFactory.unloadInstance(jarFile);
    }

    public List<String> getModuleNameList(){
        return pluginInstanceFactory.getPluginNameList();
    }

}

