package com.alibaba.jvm.sandbox.module.manager.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.lkx.jvm.sandbox.core.compoents.TraceInfoHelper;
import com.sandbox.manager.api.Components;
import com.sandbox.manager.api.MethodAdviceInvoke;
import com.sandbox.manager.api.AdviceNameDefinition;
import org.kohsuke.MetaInfServices;

/**
 * 统计耗时
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/13 - 14:45
 */
@MetaInfServices(Components.class)
public class DurationListener implements MethodAdviceInvoke {
    private final String PLUGIN_NAME = AdviceNameDefinition.DURATION.name();

    @Override
    public AdviceNameDefinition featureName() {
        return AdviceNameDefinition.DURATION;
    }

    @Override
    public void before(Advice advice) throws Throwable {
        TraceInfoHelper.addData(PLUGIN_NAME + ":start_time", System.currentTimeMillis());
    }

    @Override
    public void after(Advice advice) throws Throwable {
        Long startTime = (Long) TraceInfoHelper.getData(PLUGIN_NAME + ":start_time");
        long end = System.currentTimeMillis();
        TraceInfoHelper.addData(PLUGIN_NAME + ":end_time", end);
        TraceInfoHelper.addData(PLUGIN_NAME + ":duration", end - startTime);
    }
}
