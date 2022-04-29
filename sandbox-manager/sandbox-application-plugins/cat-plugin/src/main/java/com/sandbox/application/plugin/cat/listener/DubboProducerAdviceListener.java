package com.sandbox.application.plugin.cat.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.dianping.cat.message.Transaction;
import com.lkx.jvm.sandbox.core.util.AdviceUtils;
import com.lkx.jvm.sandbox.core.util.JsonUtils;
import com.sandbox.application.plugin.cat.components.trace.CatCrossProcess;
import com.sandbox.application.plugin.cat.components.trace.CatMsgConstants;
import com.sandbox.application.plugin.cat.utils.Cats;
import com.sandbox.manager.api.AdviceNameDefinition;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 动态日志的植入
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/8 - 17:25
 */
@Component
public class DubboProducerAdviceListener extends AbstractTransactionPointListener {

    @Override
    public AdviceNameDefinition featureName() {
        return AdviceNameDefinition.DUBBO_PRODUCER;
    }

    @Override
    public boolean preHandler(Advice advice) {
        return true;
        //return advice.isProcessTop() && Cat.getManager().isCatEnabled() && Cat.getManager().hasContext();
    }

    @Override
    public void before(Advice advice) throws Throwable {

        Map<String, String> contextMap = AdviceUtils.getObjectValue(advice, 1, "getAttachments", Map.class);

        if (contextMap == null || contextMap.size() == 0) {
            return;
        }

        String interfaceName = contextMap.getOrDefault("interface", "UNKNOW");

        String methodName = AdviceUtils.getObjectValue(advice, 1, "getMethodName", String.class);

        String name = interfaceName + "#" + methodName;

        Transaction transaction = CatCrossProcess.getCrossTransactionMsg(CatMsgConstants.DUBBO_PRODUCER_NAME, name, contextMap);


        Object[] arguments = AdviceUtils.getObjectValue(advice, 1, "getArguments", Object[].class);
        Object[] parameterTypes = AdviceUtils.getObjectValue(advice, 1, "getParameterTypes", Object[].class);

        StringBuilder argumentsLog = new StringBuilder("REQUEST PARAMS: ");

        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            if (argument == null) {
                continue;
            }
            argumentsLog.append(parameterTypes[i]).append(":").append(arguments[i]).append("\r\n");
        }
        Cats.logDebug(argumentsLog.toString());
        this.transactionThreadLocal.set(transaction);
    }


    @Override
    public void after(Advice advice) throws Throwable {

        Object returnObj = advice.getReturnObj();

        if (returnObj != null) {
            Cats.logDebug("result : " + JsonUtils.toJsonString(returnObj));
        }

        super.after(advice);
    }
}
