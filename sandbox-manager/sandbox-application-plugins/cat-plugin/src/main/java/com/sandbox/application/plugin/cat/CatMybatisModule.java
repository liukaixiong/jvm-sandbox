package com.sandbox.application.plugin.cat;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.model.enhance.EnhanceClassInfo;
import com.sandbox.manager.api.processors.AbstractPluginModuleDefinitionProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/19 - 15:15
 */
@Component
public class CatMybatisModule extends AbstractPluginModuleDefinitionProcessor {

    @Override
    public String pluginName() {
        return "cat-mybatis-plugin";
    }

    @Override
    public List<EnhanceClassInfo> getEnhanceClassInfos() {
        EnhanceClassInfo log = EnhanceClassInfo.builder().classPattern("org.apache.ibatis.executor.BaseExecutor").methodPatterns(EnhanceClassInfo.MethodPattern.transform("update", "query", "batch", "queryCursor")).includeSubClasses(true).build();
        return Lists.newArrayList(log);
    }

    @Override
    public Set<AdviceNameDefinition> getMethodAdviceInvoke() {
        return Sets.newHashSet(AdviceNameDefinition.CAT_MYBATIS_TRANSACTION_POINT);
    }

    @Override
    public String moduleName() {
        return "cat-plugin";
    }

}
