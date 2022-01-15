package com.alibaba.jvm.sandbox.module.manager.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.lkx.jvm.sandbox.core.compoents.TraceInfoHelper;
import com.lkx.jvm.sandbox.core.factory.GlobalFactoryHelper;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.MethodAdviceInvoke;
import com.sandbox.manager.api.Trace;
import org.springframework.stereotype.Component;

/**
 * 植入链路编号
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/13 - 16:03
 */
@Component
public class TraceIdListener implements MethodAdviceInvoke {

    private final String PLUGIN_NAME = AdviceNameDefinition.TRACE_INFO.name();

    @Override
    public AdviceNameDefinition featureName() {
        return AdviceNameDefinition.TRACE_INFO;
    }

    @Override
    public void before(Advice advice) throws Throwable {
        Trace trace = GlobalFactoryHelper.getInstance().getObject(Trace.class);
        if (trace != null) {
            TraceInfoHelper.addData(PLUGIN_NAME + ":id", trace.getId());
        }
    }
}
