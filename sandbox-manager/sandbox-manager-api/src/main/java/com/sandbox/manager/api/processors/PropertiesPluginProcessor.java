package com.sandbox.manager.api.processors;

import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.model.enhance.EnhanceClassInfo;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 公共属性注册插件执行器
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/12 - 10:24
 */
public class PropertiesPluginProcessor extends AbstractPluginModuleDefinitionProcessor {

    private final List<EnhanceClassInfo> enhanceClassInfos;

    private final Set<AdviceNameDefinition> adviceNameDefinitions;

    private final String key;

    @Override
    public String pluginName() {
        return this.key + ":common-properties-register";
    }

    public PropertiesPluginProcessor(String key, List<EnhanceClassInfo> enhanceClassInfos, Set<AdviceNameDefinition> adviceNameDefinitions) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(enhanceClassInfos, "enhanceClassInfos");
        Objects.requireNonNull(adviceNameDefinitions, "adviceNameDefinitions");

        this.key = key;
        this.enhanceClassInfos = enhanceClassInfos;
        this.adviceNameDefinitions = adviceNameDefinitions;
    }

    @Override
    public List<EnhanceClassInfo> getEnhanceClassInfos() {
        return this.enhanceClassInfos;
    }

    @Override
    public Set<AdviceNameDefinition> getMethodAdviceInvoke() {
        return this.adviceNameDefinitions;
    }

    @Override
    public String moduleName() {
        return this.key;
    }

}
