package com.alibaba.jvm.sandbox.module.manager.components;

import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.module.manager.process.callback.CommandPostCallback;
import com.alibaba.jvm.sandbox.module.manager.process.callback.ListCommandPostCallback;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 负责管理周期性的监听执行器
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 10:25
 */
public class CommandProcessManager {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private ModuleEventWatcher watcher;
    private String classNamePattern;
    private String methodPattern;
    private CommandWatcherInfoModel commandInfoModel;

    public CommandProcessManager(ModuleEventWatcher watcher, CommandWatcherInfoModel commandInfoModel) {
        this.watcher = watcher;
        this.classNamePattern = commandInfoModel.getClassNamePattern();
        this.methodPattern = commandInfoModel.getMethodPattern();
        this.commandInfoModel = commandInfoModel;
    }

    /**
     * 执行全局观察
     */
    public EventWatcher invokeGlobalWatch() throws Exception {
        String command = commandInfoModel.getCommand();
        AbstractCommandInvoke commandInvoke = GroupContainerHelper.getInstance().getCommandInvoke(command, commandInfoModel);
        EventWatcher eventWatcher = commandInvoke.getEventWatcher(this.watcher);
        if (eventWatcher == null) {
            eventWatcher = new EventWatchBuilder(this.watcher)
                    .onClass(classNamePattern).includeBootstrap()
                    .includeSubClasses()
                    .onBehavior(methodPattern)
                    .onWatching()
                    .withCall()
                    .onWatch(commandInvoke);
            logger.info("注册全局事件,命令:[" + command + "] 类:" + classNamePattern + "，方法:" + methodPattern);
        }
        return eventWatcher;
    }

    /**
     * 执行调度观察
     */
    public void invokeScheduleWatch() throws Exception {
        String command = commandInfoModel.getCommand();

        CountDownLatch countDownLatch = new CountDownLatch(ObjectUtils.defaultIfNull(commandInfoModel.getInvokeNumber(), Integer.MAX_VALUE));


        ListCommandPostCallback listCommandPostCallback = new ListCommandPostCallback();

        listCommandPostCallback.setCallback(new CommandPostCallback() {
            @Override
            public void callback(CommandWatcherInfoModel commandInfoModel, Object req) {
                countDownLatch.countDown();
                commandInfoModel.setHowManyCount(countDownLatch.getCount());
            }
        });

        AbstractCommandInvoke commandInvoke = GroupContainerHelper.getInstance().getCommandInvoke(command, commandInfoModel);
        commandInvoke.setCommandPostProcess(listCommandPostCallback);

        Long timeOut = ObjectUtils.defaultIfNull(commandInfoModel.getTimeOut(), -1L);

        EventWatcher watcher = new EventWatchBuilder(this.watcher)
                .onClass(classNamePattern).includeBootstrap()
                .includeSubClasses()
                .onBehavior(methodPattern)
                .onWatching()
                .withCall()
                .onWatch(commandInvoke);
        try {
            if (timeOut == -1) {
                countDownLatch.await();
            } else {
                countDownLatch.await(timeOut, TimeUnit.MILLISECONDS);
            }
            logger.info("该监听执行完毕");
        } finally {
            watcher.onUnWatched();
        }
    }
}
