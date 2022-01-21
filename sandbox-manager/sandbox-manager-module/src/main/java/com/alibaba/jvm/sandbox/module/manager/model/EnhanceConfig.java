package com.alibaba.jvm.sandbox.module.manager.model;

import com.sandbox.manager.api.AdviceNameDefinition;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/12 - 11:00
 */
public class EnhanceConfig {

    private String classPattern;

    private String methodNames;

    private List<String> adviceNames;

    private boolean includeSubClasses = true;

    public boolean isIncludeSubClasses() {
        return includeSubClasses;
    }

    public void setIncludeSubClasses(boolean includeSubClasses) {
        this.includeSubClasses = includeSubClasses;
    }


    public String getClassPattern() {
        return classPattern;
    }

    public void setClassPattern(String classPattern) {
        this.classPattern = classPattern;
    }

    public String getMethodNames() {
        return methodNames;
    }

    public void setMethodNames(String methodNames) {
        this.methodNames = methodNames;
    }

    public List<String> getAdviceNames() {
        return adviceNames;
    }

    public void setAdviceNames(List<String> adviceNames) {
        this.adviceNames = adviceNames;
    }

    public Set<AdviceNameDefinition> toAdviceNameDefinitions() {
        return CollectionUtils.emptyIfNull(adviceNames).stream().filter((name) -> EnumUtils.getEnum(AdviceNameDefinition.class, name) != null).map(AdviceNameDefinition::valueOf).collect(Collectors.toSet());
    }
}
