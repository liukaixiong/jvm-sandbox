package com.lkx.jvm.sandbox.core.model.command;

import java.util.Map;

/**
 * 命令基础对象
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/22 - 11:08
 */
public class CommandModel {
    /**
     * 执行的指令
     */
    private String command;

    private String classNamePattern;

    private String methodPattern;

    /**
     * 拓展参数
     */
    private Map<String, Object> extData;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getClassNamePattern() {
        return classNamePattern;
    }

    public void setClassNamePattern(String classNamePattern) {
        this.classNamePattern = classNamePattern;
    }

    public String getMethodPattern() {
        return methodPattern;
    }

    public void setMethodPattern(String methodPattern) {
        this.methodPattern = methodPattern;
    }

    public Map<String, Object> getExtData() {
        return extData;
    }

    public void setExtData(Map<String, Object> extData) {
        this.extData = extData;
    }
}
