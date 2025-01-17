package com.alibaba.jvm.sandbox.module.manager.components;

import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.module.manager.process.callback.CommandPostCallback;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 抽象的命令执行器
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 14:44
 */
public abstract class AbstractCommandInvoke extends AdviceListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected CommandWatcherInfoModel commandObject;

    private CommandPostCallback commandPostProcess;

    /**
     * 定义命令列表
     *
     * @return
     */
    public abstract CommandEnums.Watcher commandName();

    /**
     * 每个执行对象都对应着一个命令编号
     *
     * @param commandObject 命令请求对象
     */
    public AbstractCommandInvoke(CommandWatcherInfoModel commandObject) {
        Assert.notNull(commandObject, "commandObject is not null!");
        this.commandObject = commandObject;
    }

    public void setCommandPostProcess(CommandPostCallback commandPostProcess) {
        this.commandPostProcess = commandPostProcess;
    }

    /**
     * 如果你有重构EventWatcher的想法
     *
     * @return
     */
    protected EventWatcher getEventWatcher(ModuleEventWatcher watcher) {
        return null;
    }

    /**
     * 将执行的结果信息发送到对应的服务
     *
     * @param obj
     */
    protected void sendObjectLog(Object obj) {
        this.commandPostProcess.callback(commandObject, obj);
    }

    public CommandWatcherInfoModel getCommandObject() {
        return commandObject;
    }

    protected void stop() {

    }
}
