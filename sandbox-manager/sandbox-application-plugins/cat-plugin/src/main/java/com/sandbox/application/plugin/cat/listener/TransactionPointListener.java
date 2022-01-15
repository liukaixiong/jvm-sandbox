package com.sandbox.application.plugin.cat.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.MethodAdviceInvoke;

/**
 * 事务打点
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/11 - 18:59
 */
public class TransactionPointListener implements MethodAdviceInvoke {
    @Override
    public AdviceNameDefinition featureName() {
        return AdviceNameDefinition.CAT_TRANSACTION_POINT;
    }

    private final ThreadLocal<Transaction> transactionThreadLocal = new ThreadLocal<>();

    @Override
    public void before(Advice advice) throws Throwable {
        if (!Cat.getManager().isCatEnabled()) {
            return;
        }

        String methodLogo = advice.getBehavior().getDeclaringClass().getName() + "#" + advice.getBehavior().getName();
        Transaction transaction = Cat.newTransaction("interface", methodLogo);
        transactionThreadLocal.set(transaction);
    }

    @Override
    public void after(Advice advice) throws Throwable {
        if (!Cat.getManager().isCatEnabled()) {
            return;
        }

        Transaction transaction = transactionThreadLocal.get();
        Throwable throwable = advice.getThrowable();
        if (throwable != null) {
            transaction.setStatus(advice.getThrowable());
            Cat.logError(throwable.getMessage(), throwable);
        } else {
            transaction.setStatus(Transaction.SUCCESS);
        }
        transaction.complete();
        transactionThreadLocal.remove();
    }
}
