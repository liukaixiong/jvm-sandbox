package com.alibaba.jvm.sandbox.module.manager.components;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.lkx.jvm.sandbox.core.model.command.CommandInfoModel;
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
public class EventWatcherProcess {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ModuleEventWatcher watcher;
    private String classNamePattern;
    private String methodPattern;
    private CommandInfoModel commandInfoModel;

    public EventWatcherProcess(ModuleEventWatcher watcher, CommandInfoModel commandInfoModel) {
        this.watcher = watcher;
        this.classNamePattern = commandInfoModel.getClassNamePattern();
        this.methodPattern = commandInfoModel.getMethodPattern();
        this.commandInfoModel = commandInfoModel;
    }

    /**
     * 执行全局观察
     */
    public void invokeGlobalWatch() {
        new EventWatchBuilder(this.watcher)
                .onClass(classNamePattern).includeBootstrap()
                .includeSubClasses()
                .onBehavior(methodPattern)
                .onWatching()
                .withCall()
                .onWatch(
                        // 这一部分到时候得抽出去
                        new AdviceListener() {
                            @Override
                            protected void before(Advice advice) throws Throwable {
                                logger.info("监听到:" + advice.getBehavior().getDeclaringClass().getName() + "#" + advice.getBehavior().getName() + " 被执行!");
                            }

                            @Override
                            protected void beforeCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {

                            }
                        });
    }

    /**
     * 执行调度观察
     */
    public void invokeScheduleWatch() {

        CountDownLatch countDownLatch = new CountDownLatch(ObjectUtils.defaultIfNull(commandInfoModel.getInvokeNumber(), Integer.MAX_VALUE));

        Long timeOut = ObjectUtils.defaultIfNull(commandInfoModel.getTimeOut(), -1L);

        EventWatcher watcher = new EventWatchBuilder(this.watcher)
                .onClass(classNamePattern).includeBootstrap()
                .includeSubClasses()
                .onBehavior(methodPattern)
                .onWatching()
                .withCall()
                .onWatch(new AdviceListener() {
                    private ThreadLocal<Boolean> threadLocal = new InheritableThreadLocal<>();

                    @Override
                    protected void before(Advice advice) throws Throwable {
                        logger.info("监听到:" + advice.getBehavior().getDeclaringClass().getName() + "#" + advice.getBehavior().getName() + " 被执行!");
                        if (advice.isProcessTop() && threadLocal.get() == null || !threadLocal.get()) {
                            threadLocal.set(true);
                            StringBuilder sb = new StringBuilder("\r\n");
                            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                            for (int i = 7; i < stackTrace.length; i++) {
                                sb.append(stackTrace[i].toString());
                                sb.append("\r\n");
                            }
                            countDownLatch.countDown();
                            long count = countDownLatch.getCount();
                            // 希望定时更新
                            commandInfoModel.setHowManyCount(count);
                            logger.info("方法栈 :" + sb.toString() + " 剩余次数 : " + count);
                        }
                    }

                    @Override
                    protected void beforeCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {

                    }
                });

        try {
            logger.info("时刻等待被唤醒");
            if (timeOut == -1) {
                countDownLatch.await();
            } else {
                countDownLatch.await(timeOut, TimeUnit.MILLISECONDS);
            }
            logger.info("该监听执行完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("监听异常", e);
        } finally {
            watcher.onUnWatched();
        }
    }
}
