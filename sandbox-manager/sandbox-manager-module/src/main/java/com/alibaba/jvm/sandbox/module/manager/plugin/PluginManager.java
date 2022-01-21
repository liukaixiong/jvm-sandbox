package com.alibaba.jvm.sandbox.module.manager.plugin;

import com.alibaba.jvm.sandbox.module.manager.consts.ManagerConstants;
import com.alibaba.jvm.sandbox.module.manager.model.PluginEventWatcherInfo;
import com.alibaba.jvm.sandbox.module.manager.model.PluginListModel;
import com.google.common.base.Stopwatch;
import com.lkx.jvm.sandbox.core.classloader.ManagerClassLoader;
import com.lkx.jvm.sandbox.core.factory.JarManager;
import com.lkx.jvm.sandbox.core.util.FileUtils;
import com.sandbox.manager.api.processors.AbstractPluginModuleDefinitionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import sun.misc.ClassLoaderUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 插件管理器,负责管理插件的生命周期
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/17 - 15:59
 */
@Component
public class PluginManager implements ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JarManager jarManager = new JarManager();

    private ApplicationContext applicationContext;

    private final Map<String, PluginInfo> pluginCoreModuleCache = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 获取未被加载的插件
     *
     * @param pluginPath
     * @return
     */
    public PluginListModel getPluginList(String pluginPath) {
        PluginListModel pluginListModel = new PluginListModel();
        List<String> unloadList = jarManager.loadJarObjectFile(pluginPath).stream().map(File::getName).collect(Collectors.toList());
        ArrayList<String> loadList = new ArrayList<>(pluginCoreModuleCache.keySet());
        List<PluginEventWatcherInfo> pluginInfoList = pluginCoreModuleCache.values().stream().map(PluginInfo::getPluginProcessorWatcherService).map(PluginProcessorWatcherService::getPluginEventList).flatMap(Collection::stream).collect(Collectors.toList());
        pluginListModel.setLoadComplete(loadList);
        pluginListModel.setUnload(unloadList);
        pluginListModel.setPluginEventWatcherInfoList(pluginInfoList);
        return pluginListModel;
    }

    /**
     * 加载插件
     *
     * @param pluginPath  插件的路径
     * @param pluginNames 需要加载的插件的名称
     */
    public void loadPlugin(String pluginPath, List<String> pluginNames, PluginProcessorWatcherService pluginLifeCycle) {

        List<File> jarFileList = jarManager.loadJarObjectFile(pluginPath);

        // 开始构建工厂
        for (File jarFile : jarFileList) {
            String jarKey = getJarKey(jarFile);

            if (pluginNames != null && !pluginNames.isEmpty()) {
                if (!pluginNames.contains(jarKey)) {
                    continue;
                }
            }

            unloadInstance(jarKey);

            ManagerClassLoader managerClassLoader = new ManagerClassLoader(jarKey, new URL[]{builderUrl(jarFile)});

            // 构建插件的上下文
            AnnotationConfigApplicationContext pluginApplicationContext = new AnnotationConfigApplicationContext();
            pluginApplicationContext.setClassLoader(managerClassLoader);
            pluginApplicationContext.setParent(this.applicationContext);

            Stopwatch started = Stopwatch.createStarted();

            PluginApplicationProperties applicationProperties = getApplicationProperties(managerClassLoader);
            pluginApplicationContext.scan(ManagerConstants.PLUGIN_PACKAGE);
            pluginApplicationContext.refresh();
            long nanos = started.elapsed(TimeUnit.MILLISECONDS);
            log.debug("工厂刷新耗时:{}", nanos);

            registerPluginInfo(jarKey, applicationProperties, pluginLifeCycle, pluginApplicationContext);

            if (pluginLifeCycle != null) {
                // 由于这里可能是配置文件注册的，工厂内部肯定是找不到的。
                Map<String, AbstractPluginModuleDefinitionProcessor> pluginModuleDefinitionProcessorMap = pluginApplicationContext.getBeansOfType(AbstractPluginModuleDefinitionProcessor.class);
                pluginLifeCycle.initialization(new ArrayList<>(pluginModuleDefinitionProcessorMap.values()));
            }
        }
    }

    private PluginApplicationProperties getApplicationProperties(ClassLoader classLoader) {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties(ManagerConstants.PLUGIN_PROPERTIES_NAME, classLoader);
            return PluginApplicationProperties.builder(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void registerPluginInfo(String pluginName, PluginApplicationProperties
            applicationProperties, PluginProcessorWatcherService pluginLifeCycle) {
        registerPluginInfo(pluginName, applicationProperties, pluginLifeCycle, null);
    }

    public void registerPluginInfo(String pluginName, PluginApplicationProperties
            applicationProperties, PluginProcessorWatcherService pluginLifeCycle, AnnotationConfigApplicationContext
                                           pluginApplicationContext) {
        PluginInfo pluginInfo = new PluginInfo();
        pluginInfo.setModuleName(pluginName);
        pluginInfo.setPluginContext(pluginApplicationContext);
        pluginInfo.setApplicationProperties(applicationProperties);
        pluginInfo.setPluginProcessorWatcherService(pluginLifeCycle);
        pluginCoreModuleCache.put(pluginName, pluginInfo);

        log.info("加载应用 {}  完成", pluginName);
    }

    /**
     * 从插件中查找特定的类实例
     *
     * @param clazz 类信息
     * @param <T>
     * @return
     */
    public <T> List<T> getObjectList(Class<T> clazz) {
        Set<T> beanList = new HashSet<>();

        // 从主工厂获取
        Map<String, T> beansOfType = this.applicationContext.getBeansOfType(clazz);
        Optional.of(beansOfType).ifPresent((bean) -> beanList.addAll(bean.values()));

        // 从插件工厂获取,这里需要注意的一点是插件工厂如果从自身容器获取不到对象的话,会往主实例中获取
        for (PluginInfo value : pluginCoreModuleCache.values()) {
            if (value.getPluginContext() != null) {
                Optional.of(value.getPluginContext().getBeansOfType(clazz)).ifPresent((bean) -> beanList.addAll(bean.values()));
            }
        }

        return new ArrayList<>(beanList);
    }

    private URL builderUrl(File file) {
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

    public void unloadInstance(String jarFileName) {
        PluginInfo pluginInfo = pluginCoreModuleCache.remove(jarFileName);
        // 如果之前已经被加载过，那么首先得考虑把之前的加载给干掉。
        // 不过有一个很大的问题，假设通过这个类加载器得到的类被缓存到其他地方了，那么正常来讲也要先干掉，不然会被一直引用着，类始终在增长。
        // 尽可能的将插件的操作控制在这个类中
        if (pluginInfo != null) {
            // 清理监听
            pluginInfo.getPluginProcessorWatcherService().destroy();
            AnnotationConfigApplicationContext pluginContext = pluginInfo.getPluginContext();
            if (pluginContext != null) {
                ClassLoader classLoader = pluginContext.getClassLoader();
                if (classLoader instanceof URLClassLoader) {
                    ClassLoaderUtil.releaseLoader((URLClassLoader) classLoader);
                    try {
                        ((URLClassLoader) classLoader).close();
                        log.info("清理插件: {} 的加载器", jarFileName);
                    } catch (IOException e) {
                        log.error("load error", e);
                    }
                }
                pluginContext.close();
            }
            log.debug(">>> 卸载插件 : {}", jarFileName);
        }
    }

    /**
     * 支持文件上传类型
     *
     * @param inputStream
     */
    public void loadPlugin(InputStream inputStream) {

    }

    private String getJarKey(File jarFile) {
        return jarFile.getName();
    }

    /**
     * 插件信息
     */
    public static class PluginInfo {

        private String moduleName;

        private String jarSign;

        private PluginApplicationProperties applicationProperties;

        private PluginProcessorWatcherService pluginProcessorWatcherService;

        private AnnotationConfigApplicationContext pluginContext;

        public PluginApplicationProperties getApplicationProperties() {
            return applicationProperties;
        }

        public void setApplicationProperties(PluginApplicationProperties applicationProperties) {
            this.applicationProperties = applicationProperties;
        }

        public String getModuleName() {
            return moduleName;
        }

        public PluginProcessorWatcherService getPluginProcessorWatcherService() {
            return pluginProcessorWatcherService;
        }

        public void setPluginProcessorWatcherService(PluginProcessorWatcherService pluginProcessorWatcherService) {
            this.pluginProcessorWatcherService = pluginProcessorWatcherService;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public String getJarSign() {
            return jarSign;
        }

        public void setJarSign(String jarSign) {
            this.jarSign = jarSign;
        }

        public AnnotationConfigApplicationContext getPluginContext() {
            return pluginContext;
        }

        public void setPluginContext(AnnotationConfigApplicationContext pluginContext) {
            this.pluginContext = pluginContext;
        }

    }
}
