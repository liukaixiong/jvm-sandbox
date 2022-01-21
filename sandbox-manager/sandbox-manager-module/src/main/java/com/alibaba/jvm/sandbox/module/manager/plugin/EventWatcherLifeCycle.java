package com.alibaba.jvm.sandbox.module.manager.plugin;

import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.module.manager.model.PluginEventWatcherInfo;
import com.lkx.jvm.sandbox.core.listener.RouterAdviceListener;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.model.enhance.EnhanceClassInfo;
import com.sandbox.manager.api.processors.AbstractPluginModuleDefinitionProcessor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 事件监听生命周期管理器
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/30 - 15:08
 */
public class EventWatcherLifeCycle implements PluginProcessorWatcherService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final List<EventWatcher> eventWatcherList = new ArrayList<>();
    private final List<PluginEventWatcherInfo> pluginEventWatcherInfoList = new ArrayList<>();

    private final ModuleEventWatcher moduleEventWatcher;

    public EventWatcherLifeCycle(ModuleEventWatcher moduleEventWatcher) {
        this.moduleEventWatcher = moduleEventWatcher;
    }

    @Override
    public List<PluginEventWatcherInfo> getPluginEventList() {
        return pluginEventWatcherInfoList;
    }

    @Override
    public void initialization(List<AbstractPluginModuleDefinitionProcessor> pluginModuleDefinitionProcessor) {
        for (AbstractPluginModuleDefinitionProcessor abstractPluginModuleDefinitionProcessor : pluginModuleDefinitionProcessor) {
            // 将注册好的数据拆分成两份,一份监听对象,一份数据对象
            ImmutablePair<List<PluginEventWatcherInfo>, List<EventWatcher>> pair = registerPluginWatch(abstractPluginModuleDefinitionProcessor);
            eventWatcherList.addAll(pair.getRight());
            pluginEventWatcherInfoList.addAll(pair.getLeft());
        }
    }

    @Override
    public void destroy() {
        // 存在则批量销毁
        eventWatcherList.forEach(EventWatchBuilder.IBuildingForUnWatching::onUnWatched);
        pluginEventWatcherInfoList.forEach((info) -> log.debug("卸载监听 :  {} #  {}", info.getClassPattern(), info.getMethodName()));
    }

    /**
     * 注册插件观察
     *
     * @param processor
     * @return
     */
    public ImmutablePair<List<PluginEventWatcherInfo>, List<EventWatcher>> registerPluginWatch(AbstractPluginModuleDefinitionProcessor processor) {

        List<EnhanceClassInfo> enhanceClassInfos = processor.getEnhanceClassInfos();
        String moduleName = processor.moduleName();

        if (CollectionUtils.isEmpty(enhanceClassInfos)) {
            log.warn("enhance models is empty, plugin type is :" + moduleName);
            return null;
        }

        List<PluginEventWatcherInfo> watcherInfoList = new ArrayList<>();
        List<EventWatcher> eventWatcherList = new ArrayList<>();

        Set<AdviceNameDefinition> methodAdviceInvoke = processor.getMethodAdviceInvoke();

        String pluginName = processor.pluginName();

        // 开始构建EventWatch对象
        for (EnhanceClassInfo eci : enhanceClassInfos) {

            PluginEventWatcherInfo pluginEventWatcherInfo = new PluginEventWatcherInfo();
            pluginEventWatcherInfo.setModuleName(moduleName);
            pluginEventWatcherInfo.setPluginName(pluginName);
            pluginEventWatcherInfo.setIncludeSubClasses(eci.isIncludeSubClasses());
            pluginEventWatcherInfo.setClassPattern(eci.getClassPattern());

            EventWatchBuilder.IBuildingForBehavior behavior = null;

            EventWatchBuilder.IBuildingForClass builder4Class = new EventWatchBuilder(moduleEventWatcher).onClass(eci.getClassPattern());

            if (eci.isIncludeSubClasses()) {
                builder4Class = builder4Class.includeSubClasses();
            }

            StringBuilder methods = new StringBuilder();
            for (EnhanceClassInfo.MethodPattern mp : eci.getMethodPatterns()) {
                behavior = builder4Class.onBehavior(mp.getMethodName());

                methods.append(mp.getMethodName());

                if (ArrayUtils.isNotEmpty(mp.getParameterType())) {
                    behavior.withParameterTypes(mp.getParameterType());
                }
                if (ArrayUtils.isNotEmpty(mp.getAnnotationTypes())) {
                    behavior.hasAnnotationTypes(mp.getAnnotationTypes());
                }

            }

            pluginEventWatcherInfo.setMethodName(methods.toString());

            if (behavior != null) {
                EventWatcher watcher = behavior.onWatch(new RouterAdviceListener(methodAdviceInvoke));
                int watchId = watcher.getWatchId();
                log.info("add watcher success,moduleName={},pluginName={},watcherId={}", moduleName, pluginName, watchId);
                eventWatcherList.add(watcher);
                watcherInfoList.add(pluginEventWatcherInfo);
            }
        }
        return new ImmutablePair<>(watcherInfoList, eventWatcherList);
    }

}
