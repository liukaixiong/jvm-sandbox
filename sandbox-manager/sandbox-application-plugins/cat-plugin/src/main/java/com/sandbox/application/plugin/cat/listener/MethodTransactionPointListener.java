package com.sandbox.application.plugin.cat.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.sandbox.manager.api.AdviceNameDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
        return Cat.getManager().isCatEnabled();
    }

    @Override
    public void before(Advice advice) throws Throwable {
        String methodLogo = advice.getBehavior().getDeclaringClass().getName() + "#" + advice.getBehavior().getName();
        Transaction transaction = Cat.newTransaction("interface", methodLogo);
        Stack<Transaction> stack = new Stack<>();
        stack.push(transaction);
        stackThreadLocal.set(stack);
    }

    @Override
    public void beforeCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {
        String methodLogo = callJavaClassName + "#" + callJavaMethodName;
        Transaction transaction = Cat.newTransaction("interface", methodLogo);
        stackThreadLocal.get().push(transaction);
    }

    @Override
    public void afterCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc, String callThrowJavaClassName) {
        commitTransaction(advice, stackThreadLocal.get().pop());
    }

    @Override
    public void after(Advice advice) throws Throwable {
        Transaction transaction = stackThreadLocal.get().pop();
        commitTransaction(advice, transaction);
        int stackSize = stackThreadLocal.get().size();
        if (stackSize != 0) {
            logger.warn("MethodTransactionPointListener stack un clear transaction size : {}", stackSize);
            Stack<Transaction> stack = stackThreadLocal.get();
            while (stack.size() > 0) {
                Transaction pop = stack.pop();
                commitTransaction(advice, pop);
            }
        }
        stackThreadLocal.remove();
    }
}
