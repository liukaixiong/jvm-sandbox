package com.sandbox.application.plugin.cat.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.sandbox.manager.api.AdviceNameDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Stack;

/**
 * 事务打点
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/11 - 18:59
 */
@Component
public class MethodTransactionPointListener extends AbstractTransactionPointListener {

    private final ThreadLocal<Stack<Transaction>> stackThreadLocal = new ThreadLocal<>();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public AdviceNameDefinition featureName() {
        return AdviceNameDefinition.CAT_METHOD_TRANSACTION_POINT;
    }

    @Override
    public boolean preHandler(Advice advice) {
        // 这里只想处理public方法,私有方法不做埋点统计
        return Cat.getManager().isCatEnabled() && advice.getBehavior().getModifiers() != 2;
    }

    @Override
    public void before(Advice advice) throws Throwable {
        String methodLogo = advice.getBehavior().getDeclaringClass().getName() + "#" + advice.getBehavior().getName();
        Transaction transaction = Cat.newTransaction("interface", methodLogo);
        Stack<Transaction> stack = Optional.ofNullable(stackThreadLocal.get()).orElse(new Stack<>());
        stack.push(transaction);
        stackThreadLocal.set(stack);
    }

//    @Override
//    public void beforeCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {
//        String methodLogo = callJavaClassName + "#" + callJavaMethodName;
//        Transaction transaction = Cat.newTransaction("interface", methodLogo);
//        stackThreadLocal.get().push(transaction);
//    }
//
//    @Override
//    public void afterCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc, String callThrowJavaClassName) {
//        commitTransaction(advice, stackThreadLocal.get().pop());
//    }

    @Override
    public void after(Advice advice) throws Throwable {
        /*
            关于beforeCall、afterCall方法的回调，这个玩意是要在
            com.alibaba.jvm.sandbox.module.manager.plugin.EventWatcherLifeCycle.registerPluginWatch
            方法中构建EventWatcher对象时，要调用behavior.onWatching().withCall()来触发的，如果没有指定
            那么以上方法是无法生效的。不使用withCall的原因是：
            会被重复调用，1。可能类和方法都被拦截 2. 内部调用也会拦截。所以干脆只做一层。
            另外再谈一下为什么ThreadLocal需要使用Stack对象，是因为拦截的方法可能是同一个类下面的方法，在一个线程中
            类之间的互相调用是很常见的，所以先进后出将Transaction对象进行提交。
         */
        try {
            Transaction transaction = stackThreadLocal.get().pop();
            commitTransaction(advice, transaction);
        } finally {
            int stackSize = stackThreadLocal.get().size();
            if (stackSize == 0) {
                // 防止没有被卸载
                stackThreadLocal.remove();
            }
        }

    }
}
