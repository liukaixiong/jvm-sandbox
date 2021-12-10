package com.lkx.jvm.sandbox.core.model.classloader;

import com.lkx.jvm.sandbox.core.classloader.ManagerClassLoader;
import com.lkx.jvm.sandbox.core.factory.PluginObjectFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 插件核心加载模块
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/9 - 16:29
 */
public class PluginCoreModule {

    private ManagerClassLoader classLoader;

    private File jarFile;

    private PluginObjectFactory objectFactory;

    public ManagerClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ManagerClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public File getJarFile() {
        return jarFile;
    }

    public void setJarFile(File jarFile) {
        this.jarFile = jarFile;
    }

    public PluginObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public void setObjectFactory(PluginObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }
}
