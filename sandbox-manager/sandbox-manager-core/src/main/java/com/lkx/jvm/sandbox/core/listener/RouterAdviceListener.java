package com.lkx.jvm.sandbox.core.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.lkx.jvm.sandbox.core.compoents.TraceInfoHelper;
import com.lkx.jvm.sandbox.core.exception.SkipInvokeException;
import com.lkx.jvm.sandbox.core.factory.GlobalFactoryHelper;
import com.lkx.jvm.sandbox.core.interceptor.MethodAdviceInterceptor;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.MethodAdviceInvoke;
import com.sandbox.manager.api.ThrowableConsumerSupport;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 多个AdviceListener处理器
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/8 - 15:44
 */
public class RouterAdviceListener extends AdviceListener {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final List<MethodAdviceInvoke> adviceListenerInvokes;
    private final List<MethodAdviceInterceptor> methodAdviceInterceptors;

    public RouterAdviceListener(Set<AdviceNameDefinition> adviceListenerInvokes) {
        this.adviceListenerInvokes = getInvokeList(adviceListenerInvokes);
        this.methodAdviceInterceptors = ListUtils.defaultIfNull(GlobalFactoryHelper.getInstance().getList(MethodAdviceInterceptor.class), new ArrayList<>());

    }

    private List<MethodAdviceInvoke> getInvokeList(Set<AdviceNameDefinition> adviceListenerInvokes) {
        return ListUtils.emptyIfNull(GlobalFactoryHelper.plugin().getList(MethodAdviceInvoke.class)).stream().filter((adviceName) -> adviceListenerInvokes.contains(adviceName.featureName())).collect(Collectors.toList());
    }

    @Override
    protected void before(Advice advice) throws Throwable {
        invoke(advice, adviceListener -> adviceListener.before(advice));
    }

    @Override
    protected void afterReturning(Advice advice) throws Throwable {
        invoke(advice, adviceListener -> adviceListener.afterReturning(advice));
    }

    @Override
    protected void after(Advice advice) throws Throwable {
        invoke(advice, adviceListener -> adviceListener.after(advice));
    }

    @Override
    protected void afterThrowing(Advice advice) throws Throwable {
        invoke(advice, adviceListener -> adviceListener.afterThrowing(advice));
    }

    @Override
    protected void beforeCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {
        invoke(advice, adviceListener -> adviceListener.beforeCall(advice, callLineNum, callJavaClassName, callJavaMethodName, callJavaMethodDesc));
    }

    @Override
    protected void afterCallReturning(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {
        invoke(advice, (adviceListener -> adviceListener.afterCallReturning(advice, callLineNum, callJavaClassName, callJavaMethodName, callJavaMethodDesc)));
    }

    @Override
    protected void afterCallThrowing(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc, String callThrowJavaClassName) {
        invoke(advice, (adviceListener -> adviceListener.afterCallThrowing(advice, callLineNum, callJavaClassName, callJavaMethodName, callJavaMethodDesc, callThrowJavaClassName)));
    }

    @Override
    protected void afterCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc, String callThrowJavaClassName) {
        invoke(advice, (adviceListener -> adviceListener.afterCallThrowing(advice, callLineNum, callJavaClassName, callJavaMethodName, callJavaMethodDesc, callThrowJavaClassName)));
    }

    @Override
    protected void beforeLine(Advice advice, int lineNum) {
        invoke(advice, (adviceListener -> adviceListener.beforeLine(advice, lineNum)));
    }

    public void invoke(Advice advice, ThrowableConsumerSupport<MethodAdviceInvoke> consumer) {
        if (CollectionUtils.isNotEmpty(adviceListenerInvokes)) {
            for (MethodAdviceInvoke adviceListener : adviceListenerInvokes) {
                Map<String, Object> context = new HashMap<>();
                try {
                    if (adviceListener.preHandler(advice)) {
                        invokeIntercept((intercept) -> intercept.before(advice, adviceListener, context));
                        consumer.accept(adviceListener);
                        invokeIntercept((intercept) -> intercept.after(advice, adviceListener, context));
                    }
                } catch (SkipInvokeException e) {
                    log.debug("跳过执行: {} ",e.getMessage());
                } catch (Throwable e) {
                    TraceInfoHelper.addData(adviceListener.featureName() + ":error", e.getMessage());
                    log.error("plugin invoke error :" + adviceListener.featureName(), e);
                    invokeIntercept((intercept) -> intercept.error(advice, adviceListener, context, e));
                }
            }
        }
    }

    private void invokeIntercept(Consumer<MethodAdviceInterceptor> supplier) {
        for (MethodAdviceInterceptor methodAdviceConsumer : this.methodAdviceInterceptors) {
            supplier.accept(methodAdviceConsumer);
        }
    }

//    public void callInvoke(Advice advice, Consumer<MethodAdviceInvoke> consumer) {
//        try {
//            invoke(advice, consumer);
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//    }


}
