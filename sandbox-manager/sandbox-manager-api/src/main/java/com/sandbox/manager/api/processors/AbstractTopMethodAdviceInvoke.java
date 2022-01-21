package com.sandbox.manager.api.processors;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.sandbox.manager.api.MethodAdviceInvoke;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/19 - 13:46
 */
public abstract class AbstractTopMethodAdviceInvoke implements MethodAdviceInvoke {

    @Override
    public void before(Advice advice) throws Throwable {

    }

    @Override
    public void afterReturning(Advice advice) throws Throwable {

    }

    @Override
    public void after(Advice advice) throws Throwable {

    }

    @Override
    public void afterThrowing(Advice advice) throws Throwable {

    }

    @Override
    public void beforeCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {

    }

    @Override
    public void afterCallReturning(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {

    }

    @Override
    public void afterCallThrowing(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc, String callThrowJavaClassName) {

    }

    @Override
    public void afterCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc, String callThrowJavaClassName) {

    }

    @Override
    public void beforeLine(Advice advice, int lineNum) {

    }
}
