package com.sandbox.application.plugin.cat;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.Components;
import com.sandbox.manager.api.model.enhance.EnhanceClassInfo;
import com.sandbox.manager.api.processors.AbstractPluginModuleDefinitionProcessor;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;


/**
 * CAT相关的数据收集器
 *
 * @author liukx
 * @date 2021/12/7 19:00
 */
@MetaInfServices(Components.class)
public class CatTransactionModule extends AbstractPluginModuleDefinitionProcessor {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String moduleName() {
        return "cat-plugin";
    }

    @Override
    public String pluginName() {
        return "cat-transaction-plugin";
    }

    @Override
    public List<EnhanceClassInfo> getEnhanceClassInfos() {
        EnhanceClassInfo log = EnhanceClassInfo.builder().classPattern("org.slf4j.Logger").methodPatterns(EnhanceClassInfo.MethodPattern.transform("info", "debug")).includeSubClasses(true).build();
        return Lists.newArrayList(log);
    }

    @Override
    public Set<AdviceNameDefinition> getMethodAdviceInvoke() {
        return Sets.newHashSet(AdviceNameDefinition.ERROR_INFO);
    }

}