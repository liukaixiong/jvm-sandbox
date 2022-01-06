package com.alibaba.jvm.sandbox.module.manager.process;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.module.manager.components.AbstractCommandInvoke;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;

/**
 * 方法的链路结果监控
 * 将方法的链路结果记录，并输出为json内容。
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/2 - 18:00
 */
public class MethodTraceResultProcess extends AbstractCommandInvoke {


    /**
     * 每个执行对象都对应着一个命令编号
     *
     * @param commandObject 命令请求对象
     */
    public MethodTraceResultProcess(CommandWatcherInfoModel commandObject) {
        super(commandObject);
    }

    @Override
    public CommandEnums.Watcher commandName() {
        return CommandEnums.Watcher.method_trace_result;
    }

    @Override
    protected void after(Advice advice) throws Throwable {
        advice.getBehavior().getName();
        super.after(advice);
    }

    @Override
    protected void beforeCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {
        super.beforeCall(advice, callLineNum, callJavaClassName, callJavaMethodName, callJavaMethodDesc);
    }

    @Override
    protected void afterCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc, String callThrowJavaClassName) {
        super.afterCall(advice, callLineNum, callJavaClassName, callJavaMethodName, callJavaMethodDesc, callThrowJavaClassName);
    }

}
