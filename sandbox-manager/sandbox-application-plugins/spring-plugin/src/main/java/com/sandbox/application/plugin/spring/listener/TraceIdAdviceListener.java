package com.sandbox.application.plugin.spring.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.lkx.jvm.sandbox.core.factory.GlobalFactoryHelper;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.MethodAdviceInvoke;
import com.sandbox.manager.api.Trace;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * 结果绑定链路编号输出
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/17 - 14:41
 */
@Component
public class TraceIdAdviceListener implements MethodAdviceInvoke {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public AdviceNameDefinition featureName() {
        return AdviceNameDefinition.TRACE_ID;
    }

    @Override
    public void after(Advice advice) throws Throwable {
        if (!advice.isProcessTop()) {
            return;
        }

        Trace trace = GlobalFactoryHelper.plugin().getObject(Trace.class);

        if (trace != null) {
            String currentMessageId = trace.getId();
            Object returnObj = advice.getReturnObj();
            if(returnObj == null){
                return ;
            }
            // logger.info(" trace id : "+currentMessageId);
            if (returnObj instanceof Map) {
                ((Map<Object, Object>) returnObj).put("traceId", currentMessageId);
            } else if (returnObj instanceof Collection || returnObj.getClass().isPrimitive()) {
                // 暂不处理
            } else {
                BeanUtils.setProperty(returnObj, "traceId", currentMessageId);
            }
        }
    }

}
