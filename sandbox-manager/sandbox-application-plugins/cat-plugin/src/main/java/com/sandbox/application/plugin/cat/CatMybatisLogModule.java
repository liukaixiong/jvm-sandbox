package com.sandbox.application.plugin.cat;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.model.enhance.EnhanceClassInfo;
import com.sandbox.manager.api.processors.AbstractPluginModuleDefinitionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CatMybatisLogModule extends AbstractPluginModuleDefinitionProcessor {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String moduleName() {
        return "cat-plugin";
    }

    @Override
    public String pluginName() {
        return "cat-mybatis-log-plugin";
    }

    @Override
    public List<EnhanceClassInfo> getEnhanceClassInfos() {
        EnhanceClassInfo log = EnhanceClassInfo.builder().classPattern("org.apache.ibatis.logging.Log").methodPatterns(EnhanceClassInfo.MethodPattern.transform("info", "debug", "warn", "error")).includeSubClasses(true).build();
        return Lists.newArrayList(log);
    }

    @Override
    public Set<AdviceNameDefinition> getMethodAdviceInvoke() {
        return Sets.newHashSet(AdviceNameDefinition.ERROR_INFO, AdviceNameDefinition.PRINT_LOG);
    }

}
