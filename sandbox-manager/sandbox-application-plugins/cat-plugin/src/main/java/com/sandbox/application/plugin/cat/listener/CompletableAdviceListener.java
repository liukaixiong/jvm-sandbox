package com.sandbox.application.plugin.cat.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.dianping.cat.Cat;
import com.sandbox.application.plugin.cat.components.trace.CatSupplier;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.MethodAdviceInvoke;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * 动态日志的植入
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/8 - 17:25
 */
@Component
public class CompletableAdviceListener implements MethodAdviceInvoke {

    @Override
    public AdviceNameDefinition featureName() {
        return AdviceNameDefinition.CAT_CompletableFuture_POINT;
    }

    @Override
    public boolean preHandler(Advice advice) {
//        return true;
        return advice.isProcessTop() && Cat.getManager().isCatEnabled() && Cat.getManager().hasContext() && advice.getParameterArray().length > 0;
    }

    @Override
    public void before(Advice advice) throws Throwable {
        Object f = advice.getParameterArray()[1];
        if (f instanceof Supplier) {
            advice.getParameterArray()[1] = new CatSupplier<>(((Supplier<?>) f));
        }
    }
}
