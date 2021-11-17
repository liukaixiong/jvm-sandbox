package com.alibaba.jvm.sandbox.module.manager.components;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.module.manager.process.callback.CommandPostCallback;
import com.alibaba.jvm.sandbox.module.manager.util.PropertyUtil;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandInfoModel;
import com.lkx.jvm.sandbox.core.model.command.CommandLogResponse;
import com.lkx.jvm.sandbox.core.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * 抽象的命令执行器
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 14:44
 */
public abstract class AbstractCommandInvoke extends AdviceListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private CommandInfoModel commandObject;

    private CommandPostCallback commandPostProcess;

    /**
     * 定义命令列表
     *
     * @return
     */
    public abstract CommandEnums commandName();

    /**
     * 每个执行对象都对应着一个命令编号
     *
     * @param commandObject
     */
    public AbstractCommandInvoke(CommandInfoModel commandObject) {
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

    public CommandInfoModel getCommandObject() {
        return commandObject;
    }
}
