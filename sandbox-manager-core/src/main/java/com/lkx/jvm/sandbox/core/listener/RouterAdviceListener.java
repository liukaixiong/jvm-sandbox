package com.lkx.jvm.sandbox.core.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.lkx.jvm.sandbox.core.compoents.TraceInfoHelper;
import com.lkx.jvm.sandbox.core.factory.GlobalFactoryHelper;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.MethodAdviceInvoke;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 多个AdviceListener处理器
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/8 - 15:44
 */
public class RouterAdviceListener extends AdviceListener {
    private Logger log = LoggerFactory.getLogger(getClass());
    private List<MethodAdviceInvoke> adviceListenerInvokes;

    public RouterAdviceListener(Set<AdviceNameDefinition> adviceListenerInvokes) {
        this.adviceListenerInvokes = getInvokeList(adviceListenerInvokes);
    }

    private List<MethodAdviceInvoke> getInvokeList(Set<AdviceNameDefinition> adviceListenerInvokes) {
        return GlobalFactoryHelper.getInstance().getList(MethodAdviceInvoke.class).stream().filter((adviceName) -> adviceListenerInvokes.contains(adviceName.featureName())).collect(Collectors.toList());
    }

    @Override
    protected void before(Advice advice) throws Throwable {
        invoke(adviceListener -> adviceListener.before(advice));
    }

    @Override
    protected void afterReturning(Advice advice) throws Throwable {
        invoke(adviceListener -> adviceListener.afterReturning(advice));
    }

    @Override
    protected void after(Advice advice) throws Throwable {
        invoke(adviceListener -> adviceListener.after(advice));
    }

    @Override
    protected void afterThrowing(Advice advice) throws Throwable {
        invoke(adviceListener -> adviceListener.afterThrowing(advice));
    }

    @Override
    protected void beforeCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {
        callInvoke(adviceListener -> adviceListener.beforeCall(advice, callLineNum, callJavaClassName, callJavaMethodName, callJavaMethodDesc));
    }

    @Override
    protected void afterCallReturning(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {
        callInvoke((adviceListener -> adviceListener.afterCallReturning(advice, callLineNum, callJavaClassName, callJavaMethodName, callJavaMethodDesc)));
    }

    @Override
    protected void afterCallThrowing(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc, String callThrowJavaClassName) {
        callInvoke((adviceListener -> adviceListener.afterCallThrowing(advice, callLineNum, callJavaClassName, callJavaMethodName, callJavaMethodDesc, callThrowJavaClassName)));
    }

    @Override
    protected void afterCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc, String callThrowJavaClassName) {
        callInvoke((adviceListener -> adviceListener.afterCallThrowing(advice, callLineNum, callJavaClassName, callJavaMethodName, callJavaMethodDesc, callThrowJavaClassName)));
    }

    @Override
    protected void beforeLine(Advice advice, int lineNum) {
        callInvoke((adviceListener -> adviceListener.beforeLine(advice, lineNum)));
    }

    public void invoke(Consumer<MethodAdviceInvoke> consumer) throws Throwable {
        if (CollectionUtils.isNotEmpty(adviceListenerInvokes)) {
            for (MethodAdviceInvoke adviceListener : adviceListenerInvokes) {
                try {
                    consumer.accept(adviceListener);
                } catch (Throwable e) {
                    TraceInfoHelper.addData(adviceListener.featureName() + ":error", e.getMessage());
                    log.error("plugin invoke error :" + adviceListener.featureName(), e);
                }
            }
        }
    }

    public void callInvoke(Consumer<MethodAdviceInvoke> consumer) {
        try {
            invoke(consumer);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    interface Consumer<T> {
        void accept(T t) throws Throwable;
    }
}
