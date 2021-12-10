package com.lkx.jvm.sandbox.core.processors;

import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.google.common.collect.Lists;
import com.lkx.jvm.sandbox.core.listener.MultipleAdviceListener;
import com.lkx.jvm.sandbox.core.model.enhance.EnhanceClassInfo;
import com.sandbox.manager.api.PluginModule;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * 插件模块定义执行器，这里主要负责定义需要增强的信息以及增强后的回调处理器。
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/8 - 14:23
 */
@MetaInfServices
public abstract class AbstractPluginModuleDefinitionProcessor implements PluginModule {
    private Logger log = LoggerFactory.getLogger(getClass());
    private List<Integer> watchIds = Lists.newCopyOnWriteArrayList();

    /**
     * 插件名称
     *
     * @return
     */
    public abstract String pluginName();

    /**
     * 获取子类传递需要增强的类
     *
     * @return
     */
    public abstract List<EnhanceClassInfo> getEnhanceClassInfos();

    /**
     * 获取切面监听对象
     *
     * @return
     */
    public abstract Set<String> getAdviceListenerName();


    public void watch(ModuleEventWatcher watcher) throws Exception {
        List<EnhanceClassInfo> enhanceClassInfos = getEnhanceClassInfos();

        if (CollectionUtils.isEmpty(enhanceClassInfos)) {
            throw new Exception("enhance models is empty, plugin type is " + pluginName());
        }
        // 开始构建EventWatch对象
        for (EnhanceClassInfo eci : enhanceClassInfos) {
            EventWatchBuilder.IBuildingForBehavior behavior = null;

            EventWatchBuilder.IBuildingForClass builder4Class = new EventWatchBuilder(watcher).onClass(eci.getClassPattern());

            if (eci.isIncludeSubClasses()) {
                builder4Class = builder4Class.includeSubClasses();
            }

            for (EnhanceClassInfo.MethodPattern mp : eci.getMethodPatterns()) {
                behavior = builder4Class.onBehavior(mp.getMethodName());
                if (ArrayUtils.isNotEmpty(mp.getParameterType())) {
                    behavior.withParameterTypes(mp.getParameterType());
                }
                if (ArrayUtils.isNotEmpty(mp.getAnnotationTypes())) {
                    behavior.hasAnnotationTypes(mp.getAnnotationTypes());
                }
            }
            if (behavior != null) {
                Set<String> adviceListenerName = getAdviceListenerName();
                int watchId = behavior.onWatch(new MultipleAdviceListener(adviceListenerName)).getWatchId();
                watchIds.add(watchId);
                log.info("add watcher success,moduleName={},pluginName={},watcherId={}", moduleName(), pluginName(), watchId);
            }

        }
    }

}
