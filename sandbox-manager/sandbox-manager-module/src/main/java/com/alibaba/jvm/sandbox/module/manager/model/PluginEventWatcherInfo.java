package com.alibaba.jvm.sandbox.module.manager.model;

/**
 * 插件监听的模型
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/31 - 11:42
 */
public class PluginEventWatcherInfo {

    private String moduleName;

    private String pluginName;

    private String classPattern;

    private boolean includeSubClasses;

    private String methodName;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getClassPattern() {
        return classPattern;
    }

    public void setClassPattern(String classPattern) {
        this.classPattern = classPattern;
    }

    public boolean isIncludeSubClasses() {
        return includeSubClasses;
    }

    public void setIncludeSubClasses(boolean includeSubClasses) {
        this.includeSubClasses = includeSubClasses;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
