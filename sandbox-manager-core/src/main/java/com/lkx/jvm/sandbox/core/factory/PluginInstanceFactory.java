package com.lkx.jvm.sandbox.core.factory;

import com.lkx.jvm.sandbox.core.classloader.ManagerClassLoader;
import com.lkx.jvm.sandbox.core.compoents.DefaultInjectResource;
import com.lkx.jvm.sandbox.core.compoents.InjectResource;
import com.lkx.jvm.sandbox.core.model.classloader.PluginCoreModule;
import com.lkx.jvm.sandbox.core.util.FileUtils;
import com.sandbox.manager.api.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 插件实例加载工厂
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/7 - 18:36
 */
public class PluginInstanceFactory {

    private Logger log = LoggerFactory.getLogger(getClass());

    private JarManager jarManager = new JarManager();

    private Map<String, PluginCoreModule> pluginCoreModuleCache = new HashMap<>();

    private InjectResource injectResource = new DefaultInjectResource();

    protected List<String> getPluginNameList(){
        return new ArrayList<>(pluginCoreModuleCache.keySet());
    }

    public <T> List<T> getPluginInstanceList(Class<T> clazz){
        List<T> instanceList = new ArrayList<>();
        pluginCoreModuleCache.values().forEach((pluginCoreModule)->{
            instanceList.addAll(pluginCoreModule.getObjectFactory().getList(clazz));
        });
        return instanceList;
    }


    protected void loadInstance(String moduleJarFilePath) {

        List<File> jarFileList = jarManager.loadJarObjectFile(moduleJarFilePath);

        for (int i = 0; i < jarFileList.size(); i++) {

            File jarFile = jarFileList.get(i);

            unloadInstance(getJarKey(jarFile));

            ManagerClassLoader managerClassLoader = new ManagerClassLoader(new URL[]{builderURL(jarFile)}, new ManagerClassLoader.Routing(
                    this.getClass().getClassLoader(),
                    "^com\\.sandbox\\.manager\\.api\\..*"
            ));

            PluginCoreModule pluginCoreModule = new PluginCoreModule();
            pluginCoreModule.setJarFile(jarFile);
            pluginCoreModule.setClassLoader(managerClassLoader);

            PluginObjectFactory pluginObjectFactory = new PluginObjectFactory();
            // 设置注入资源对象
            pluginObjectFactory.setInjectResource(this.injectResource);
            // 加载相应的对象
            pluginObjectFactory.loadObjectList(Components.class, managerClassLoader);

            pluginCoreModule.setObjectFactory(pluginObjectFactory);

            // 这里加载出来的对象是只有实例化，而没有初始化。
            pluginCoreModuleCache.put(getJarKey(jarFile), pluginCoreModule);
        }
    }

    private String getJarKey(File jarFile) {
        return jarFile.getName();
    }

    protected void unloadInstance(String jarFileName) {
        PluginCoreModule pluginCoreModule = pluginCoreModuleCache.get(jarFileName);

        // 如果之前已经被加载过，那么首先得考虑把之前的加载给干掉。
        // 不过有一个很大的问题，假设通过这个类加载器得到的类被缓存到其他地方了，那么正常来讲也要先干掉，不然会被一直引用着，类始终在增长。
        // 尽可能的将加载的类和ClassLoader绑定
        if (pluginCoreModule != null) {
            ManagerClassLoader managerClassLoader = pluginCoreModule.getClassLoader();
            try {
                managerClassLoader.close();
            } catch (IOException e) {
                // 如果关闭不了，那么可能就要完蛋了。
                log.error("clear classload error ", e);
            } finally {
                managerClassLoader = null;
            }
            pluginCoreModuleCache.remove(jarFileName);
        }
    }

    public void setInjectResource(InjectResource injectResource) {
        this.injectResource = injectResource;
    }

    private URL builderURL(File file) {
        try {
            File tempFile = File.createTempFile("manager_plugin", ".jar");
            tempFile.deleteOnExit();
            FileUtils.copyFile(file, tempFile);
            return new URL("file:" + tempFile.getPath());
        } catch (IOException e) {
            log.error("error occurred when get jar file", e);
        }
        return null;
    }
}
