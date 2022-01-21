package com.sandbox.application.plugin.cat.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.sandbox.manager.api.MethodAdviceInvoke;

import java.util.Optional;

/**
 * 抽象事物埋点类
 * <p>
 * 主要目的是为了管控ThreadLocal以及抽象共性方法
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/19 - 11:03
 */
public abstract class AbstractTransactionPointListener implements MethodAdviceInvoke {

    private final ThreadLocal<Transaction> transactionThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandler(Advice advice) {
        return advice.isProcessTop() && Cat.getManager().isCatEnabled();
    }

    @Override
    public void after(Advice advice) throws Throwable {
        Optional.ofNullable(transactionThreadLocal.get()).ifPresent((transaction) -> {
            try {
                Throwable throwable = advice.getThrowable();
                if (throwable != null) {
                    transaction.setStatus(advice.getThrowable());
                    Cat.logError(throwable.getMessage(), throwable);
                } else {
                    transaction.setStatus(Transaction.SUCCESS);
                }
                transaction.complete();
            } finally {
                transactionThreadLocal.remove();
            }
        });
    }

    public ThreadLocal<Transaction> getTransactionThreadLocal() {
        return transactionThreadLocal;
    }

    protected void commitTransaction(Advice advice, Transaction transaction) {
        Optional.ofNullable(transaction).ifPresent((tx) -> {
            Throwable throwable = advice.getThrowable();
            if (throwable != null) {
                tx.setStatus(advice.getThrowable());
                Cat.logError(throwable.getMessage(), throwable);
            } else {
                tx.setStatus(Transaction.SUCCESS);
            }
            tx.complete();
        });
    }
}
