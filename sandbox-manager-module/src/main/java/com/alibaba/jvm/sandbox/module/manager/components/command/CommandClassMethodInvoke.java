package com.alibaba.jvm.sandbox.module.manager.components.command;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 11:28
 */
public interface CommandClassMethodInvoke extends CommandApiInvoke {
    /**
     * 类名称
     *
     * @param classNamePattern
     * @return
     */
    public String setClassName(String classNamePattern);

    /**
     * 方法名称
     *
     * @param methodNamePattern
     * @return
     */
    public String setMethodName(String methodNamePattern);

}
