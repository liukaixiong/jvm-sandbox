package com.sandbox.demo.config.prop;

import java.util.List;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/12 - 15:28
 */
public class CatProperties {

    private String classPattern;

    private String methodNames;

    private List<String> abc;

    private List<String> adviceNames;

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

    public List<String> getAbc() {
        return abc;
    }

    public void setAbc(List<String> abc) {
        this.abc = abc;
    }

    public List<String> getAdviceNames() {
        return adviceNames;
    }

    public void setAdviceNames(List<String> adviceNames) {
        this.adviceNames = adviceNames;
    }
}
