package com.alibaba.jvm.sandbox.module.manager.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.lkx.jvm.sandbox.core.compoents.TraceInfoHelper;
import com.sandbox.manager.api.Components;
import com.sandbox.manager.api.MethodAdviceInvoke;
import com.sandbox.manager.api.AdviceNameDefinition;
import org.kohsuke.MetaInfServices;
import org.springframework.stereotype.Component;

/**
 * 异常监听
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/13 - 16:11
 */
@Component
@MetaInfServices(Components.class)
public class ErrorListener implements MethodAdviceInvoke {

    private final String PLUGIN_NAME = AdviceNameDefinition.ERROR_INFO.name();

    @Override
    public AdviceNameDefinition featureName() {
        return AdviceNameDefinition.ERROR_INFO;
    }

    @Override
    public void afterThrowing(Advice advice) throws Throwable {
        TraceInfoHelper.addData(PLUGIN_NAME + ":log", advice.getThrowable().getMessage());
    }
}
