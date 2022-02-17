//package com.sandbox.application.plugin.cat;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Sets;
//import com.sandbox.manager.api.AdviceNameDefinition;
//import com.sandbox.manager.api.model.enhance.EnhanceClassInfo;
//import com.sandbox.manager.api.processors.AbstractPluginModuleDefinitionProcessor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Set;
//
///**
// * @author liukaixiong
// * @Email liukx@elab-plus.com
// * @date 2022/2/15 - 13:41
// */
//@Component
//public class CatSpringComponentModule extends AbstractPluginModuleDefinitionProcessor {
//
//    private final Logger log = LoggerFactory.getLogger(getClass());
//
//    @Override
//    public String moduleName() {
//        return "cat-plugin";
//    }
//
//    @Override
//    public String pluginName() {
//        return "cat-spring-component-plugin";
//    }
//
//    @Override
//    public List<EnhanceClassInfo> getEnhanceClassInfos() {
//        EnhanceClassInfo service = EnhanceClassInfo.builder().classPattern("com.sandbox.demo.service.*").methodPatterns(EnhanceClassInfo.MethodPattern.transform("*")).includeSubClasses(true).build();
//
//
//        //EnhanceClassInfo spring = EnhanceClassInfo.builder().classPattern("org.spring*").methodPatterns(EnhanceClassInfo.MethodPattern.transform("*")).includeSubClasses(true).build();
//
//        return Lists.newArrayList(service);
//    }
//
//    @Override
//    public Set<AdviceNameDefinition> getMethodAdviceInvoke() {
//        return Sets.newHashSet(AdviceNameDefinition.CAT_METHOD_TRANSACTION_POINT);
//    }
//
//}
