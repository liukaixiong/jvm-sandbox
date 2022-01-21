package com.sandbox.application.plugin.cat.event;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.internal.DefaultTransaction;
import com.lkx.jvm.sandbox.core.util.InterfaceProxyUtils;

import java.util.Map;
import java.util.function.Function;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/26 - 18:40
 */
@Deprecated
public class UrlEvent {

    /**
     * 执行servlet类型的数据
     */
    public static void process(ModuleEventWatcher watcher) {

        Function<Advice, Transaction> function = (advice) -> {
            // 俘虏HttpServletRequest参数为傀儡
            final IHttpServletRequest httpServletRequest = InterfaceProxyUtils.puppet(
                    IHttpServletRequest.class,
                    advice.getParameterArray()[0]
            );
            return new DefaultTransaction("URL", httpServletRequest.getRequestURI());
        };

        new EventWatchBuilder(watcher)
                .onClass("javax.servlet.http.HttpServlet")
                .includeSubClasses()
                .onBehavior("service")
                .withParameterTypes(
                        "javax.servlet.http.HttpServletRequest",
                        "javax.servlet.http.HttpServletResponse"
                ).onWatch(new CatAdviceListener(function));

    }

    static class CatAdviceListener extends AdviceListener {
        private final Function<Advice, Transaction> beforeFunction;

        public CatAdviceListener(Function<Advice, Transaction> beforeFunction) {
            this.beforeFunction = beforeFunction;
        }

        @Override
        protected void before(Advice advice) throws Throwable {
            if (!advice.isProcessTop()) {
                return;
            }

            Transaction transaction = beforeFunction.apply(advice);

            advice.getProcessTop().attach(transaction);
        }

        @Override
        protected void afterReturning(Advice advice) throws Throwable {
            if (!advice.isProcessTop()) {
                return;
            }
            Transaction transaction = advice.getProcessTop().attachment();
            if (advice.getThrowable() == null) {
                transaction.setSuccessStatus();
            } else {
                transaction.setStatus(advice.getThrowable());
            }
            transaction.complete();
        }
    }

    interface IHttpServletRequest {

        @InterfaceProxyUtils.ProxyMethod(name = "getRemoteAddr")
        String getRemoteAddress();

        String getMethod();

        String getRequestURI();

        Map<String, String[]> getParameterMap();

        String getHeader(String name);

    }

}
