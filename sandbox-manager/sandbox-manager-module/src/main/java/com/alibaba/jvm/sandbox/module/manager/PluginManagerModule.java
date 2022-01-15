package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.module.manager.model.PluginListModel;
import com.alibaba.jvm.sandbox.module.manager.plugin.EventWatcherLifeCycle;
import com.alibaba.jvm.sandbox.module.manager.plugin.PluginManager;
import com.alibaba.jvm.sandbox.module.manager.util.ResponseUtils;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.factory.GlobalFactoryHelper;
import com.lkx.jvm.sandbox.core.util.CheckUtils;
import org.apache.commons.io.FileUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Collections;
import java.util.Map;

/**
 * 插件管理模块
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/10 - 18:25
 */
@MetaInfServices(Module.class)
@Information(id = Constants.PLUGIN_MANAGER_MODULE_ID, version = "0.0.1", author = "liukaixiong")
public class PluginManagerModule implements Module {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    private ModuleEventWatcher watcher;

    @Command(Constants.MODULE_PLUGIN_HTTP_LIST)
    public void list(Map<String, String> param, HttpServletResponse response) throws Exception {
        String pluginPath = getDefaultPluginPath();
        PluginManager pluginManager = getPluginManager();
        PluginListModel pluginList = pluginManager.getPluginList(pluginPath);
        ResponseUtils.writeJson(response, pluginList);
    }

    @Command(Constants.MODULE_PLUGIN_HTTP_REGISTER)
    public void register(Map<String, String> param, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pluginPath = getDefaultPluginPath();
        String pluginName = getPluginName(param);

        FileUtils.copyInputStreamToFile(request.getInputStream(), new File(pluginPath + "\\" + pluginName));

        PluginManager pluginManager = getPluginManager();
        pluginManager.loadPlugin(pluginPath, Collections.singletonList(pluginName), new EventWatcherLifeCycle(this.watcher));

        ResponseUtils.writeTrue(response);
    }

    @Command(Constants.MODULE_PLUGIN_HTTP_STOP)
    public void stop(Map<String, String> param, HttpServletResponse response) throws Exception {
        String pluginName = getPluginName(param);
        PluginManager pluginManager = getPluginManager();
        pluginManager.unloadInstance(pluginName);
        ResponseUtils.writeTrue(response);
    }

    private String getDefaultPluginPath() {
        // todo 需要替换变量
        return "E:\\study\\sandbox\\sandbox-module\\manager-plugins";
    }

    private PluginManager getPluginManager() {
        return GlobalFactoryHelper.getInstance().getObject(PluginManager.class);
    }

    /**
     * 获取插件名称
     *
     * @param param
     * @return
     */
    private String getPluginName(Map<String, String> param) {
        String pluginNameField = "pluginName";
        String pluginName = param.get(pluginNameField);
        CheckUtils.isRequireNotNull(pluginName, pluginNameField);

        if (!pluginName.endsWith(".jar")) {
            return pluginName.concat(".jar");
        }

        return pluginName;
    }
}
