package com.alibaba.jvm.sandbox.module.manager.process;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.module.manager.components.AbstractCommandInvoke;
import com.alibaba.jvm.sandbox.module.manager.components.textui.TTree;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;

/**
 * 描述: 链路追踪
 * 查看实时运行的当前方法栈链路信息
 *
 * @author liukx
 * @date 2021/11/15 16:59
 */
public class TraceModuleProcess extends AbstractCommandInvoke {


    /**
     * 每个执行对象都对应着一个命令编号
     *
     * @param commandObject
     */
    public TraceModuleProcess(CommandWatcherInfoModel commandObject) {
        super(commandObject);
    }

    @Override
    public CommandEnums.Watcher commandName() {
        return CommandEnums.Watcher.trace;
    }

    private String getTracingTitle(final Advice advice) {
        return "Tracing for : "
                + advice.getBehavior().getDeclaringClass().getName()
                + "."
                + advice.getBehavior().getName()
                + " by "
                + Thread.currentThread().getName()
                ;
    }

    private String getEnterTitle(final Advice advice) {
        return "Enter : "
                + advice.getBehavior().getDeclaringClass().getName()
                + "."
                + advice.getBehavior().getName()
                + "(...);"
                ;
    }

    @Override
    protected void before(Advice advice) throws Throwable {
        final TTree tTree;
        if (advice.isProcessTop()) {
            advice.attach(tTree = new TTree(true, getTracingTitle(advice)));
        } else {
            tTree = advice.getProcessTop().attachment();
        }
        tTree.begin(getEnterTitle(advice));
    }

    @Override
    protected void afterReturning(Advice advice) throws Throwable {
        final TTree tTree = advice.getProcessTop().attachment();
        tTree.end();
        finish(advice);
    }

    @Override
    protected void afterThrowing(Advice advice) throws Throwable {
        final TTree tTree = advice.getProcessTop().attachment();
        tTree.begin("throw:" + advice.getThrowable().getClass().getName() + "()").end();
        tTree.end();
        finish(advice);
    }

    private void finish(Advice advice) {
        if (advice.isProcessTop()) {
            final TTree tTree = advice.attachment();
            sendObjectLog(tTree.rendering());
        }
    }

    @Override
    protected void beforeCall(final Advice advice,
                              final int callLineNum,
                              final String callJavaClassName,
                              final String callJavaMethodName,
                              final String callJavaMethodDesc) {
        final TTree tTree = advice.getProcessTop().attachment();
        tTree.begin(callJavaClassName + ":" + callJavaMethodName + "(@" + callLineNum + ")");
    }

    @Override
    protected void afterCallReturning(final Advice advice,
                                      final int callLineNum,
                                      final String callJavaClassName,
                                      final String callJavaMethodName,
                                      final String callJavaMethodDesc) {
        final TTree tTree = advice.getProcessTop().attachment();
        tTree.end();
    }

    @Override
    protected void afterCallThrowing(final Advice advice,
                                     final int callLineNum,
                                     final String callJavaClassName,
                                     final String callJavaMethodName,
                                     final String callJavaMethodDesc,
                                     final String callThrowJavaClassName) {
        final TTree tTree = advice.getProcessTop().attachment();
        tTree.set(tTree.get() + "[throw " + callThrowJavaClassName + "]").end();
    }
}
