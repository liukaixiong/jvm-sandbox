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
 * servlet 处理
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/19 - 10:51
 */
@Component
public class CatServletModule extends AbstractPluginModuleDefinitionProcessor {

    @Override
    public String pluginName() {
        return "cat-url-plugin";
    }

    @Override
    public List<EnhanceClassInfo> getEnhanceClassInfos() {
        EnhanceClassInfo log = EnhanceClassInfo.builder().classPattern("javax.servlet.http.HttpServlet").methodPatterns(EnhanceClassInfo.MethodPattern.transform("service")).includeSubClasses(true).build();
        return Lists.newArrayList(log);
    }

    @Override
    public Set<AdviceNameDefinition> getMethodAdviceInvoke() {
        return Sets.newHashSet(AdviceNameDefinition.CAT_URL_TRANSACTION_POINT);
    }

    @Override
    public String moduleName() {
        return "cat-plugin";
    }
}
