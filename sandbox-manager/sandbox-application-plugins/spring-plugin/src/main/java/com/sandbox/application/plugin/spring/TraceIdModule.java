package com.sandbox.application.plugin.spring;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.model.enhance.EnhanceClassInfo;
import com.sandbox.manager.api.processors.AbstractPluginModuleDefinitionProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


/**
 * CAT相关的数据收集器
 *
 * @author liukx
 * @date 2021/12/7 19:00
 */
@Component
public class TraceIdModule extends AbstractPluginModuleDefinitionProcessor {

    @Override
    public String moduleName() {
        return "spring-plugin";
    }

    @Override
    public String pluginName() {
        return "log-id-plugin";
    }

    @Override
    public List<EnhanceClassInfo> getEnhanceClassInfos() {
        EnhanceClassInfo log = EnhanceClassInfo.builder().classPattern("org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice").methodPatterns(EnhanceClassInfo.MethodPattern.transform("beforeBodyWrite")).includeSubClasses(true).build();
        return Lists.newArrayList(log);
    }

    @Override
    public Set<AdviceNameDefinition> getMethodAdviceInvoke() {
        return Sets.newHashSet(AdviceNameDefinition.TRACE_ID);
    }

}
