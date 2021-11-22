package com.alibaba.jvm.sandbox.module.manager.process;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.module.manager.components.AbstractCommandInvoke;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;

/**
 * 查看当前方法被执行的链路栈
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/15 - 17:12
 */
public class StackProcess extends AbstractCommandInvoke {

    /**
     * 每个执行对象都对应着一个命令编号
     *
     * @param commandObject
     */
    public StackProcess(CommandWatcherInfoModel commandObject) {
        super(commandObject);
    }

    @Override
    public CommandEnums.Watcher commandName() {
        return CommandEnums.Watcher.stack;
    }

    @Override
    protected void before(Advice advice) throws Throwable {
        if (advice.isProcessTop()) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            StringBuilder methodLog = new StringBuilder();
            for (int i = 7; i < stackTrace.length; i++) {
                methodLog.append(stackTrace[i]);
                methodLog.append("\r\n");
            }
            sendObjectLog(methodLog);
        }
    }
}
