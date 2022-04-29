package com.sandbox.application.plugin.cat.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.dianping.cat.Cat;
import com.lkx.jvm.sandbox.core.util.AdviceUtils;
import com.sandbox.application.plugin.cat.components.trace.CatCrossProcess;
import com.sandbox.application.plugin.cat.components.trace.CatMsgConstants;
import com.sandbox.application.plugin.cat.utils.Cats;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.MethodAdviceInvoke;
import org.apache.commons.lang3.reflect.MethodUtils;
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
public class DubboConsumerAdviceListener implements MethodAdviceInvoke {

    @Override
    public AdviceNameDefinition featureName() {
        return AdviceNameDefinition.DUBBO_CONSUMER;
    }

    @Override
    public boolean preHandler(Advice advice) {
        return true;
        // return advice.isProcessTop() && Cat.getManager().isCatEnabled() && Cat.getManager().hasContext();
    }

    @Override
    public void before(Advice advice) throws Throwable {


        Object invoke = AdviceUtils.getObjectValue(advice, 0, "getInvoker", Object.class);
        Cat.logEvent(CatMsgConstants.DUBBO_CONSUMER_NAME,invoke.toString());

        // 挂在CAT相关的链路编号
        Map<String,String> attachmentsContext = AdviceUtils.getObjectValue(advice, 0, "getAttachments", Map.class);

        // 如果上下文中包含了CAT上下文信息，那么还是按照之前的走。【一般是客戶端已經集成過CAT的情況】
        if(attachmentsContext != null && attachmentsContext.get(Cat.Context.PARENT) == null){
            Map<String, String> msgContextMap = CatCrossProcess.getMsgContextMap();
            attachmentsContext.putAll(msgContextMap);
        }



    }
}
