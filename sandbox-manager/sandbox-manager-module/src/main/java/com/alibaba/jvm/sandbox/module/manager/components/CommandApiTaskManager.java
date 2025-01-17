package com.alibaba.jvm.sandbox.module.manager.components;

import com.alibaba.jvm.sandbox.api.listener.ext.EventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.lkx.jvm.sandbox.core.enums.CommandTaskTypeEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 命令API管理器
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 10:36
 */
public class CommandApiTaskManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 任务线程容器
     */
    private final Map<String, Thread> threadTaskCache = new ConcurrentHashMap<>();

    /**
     * 全局监听事件
     */
    private final Map<String, EventWatcher> watcherCache = new ConcurrentHashMap<>();

    /**
     * 任务详情
     */
    private final Map<String, CommandWatcherInfoModel> commandInfoCache = new ConcurrentHashMap<>();

    /**
     * 注册调度任务
     */
    public void registerScheduleTask(ModuleEventWatcher watcher, CommandWatcherInfoModel commandInfoModel) {
        commandInfoModel.setTaskType(CommandTaskTypeEnums.SCHEDULING_TASK.name());
        String taskId = getTaskId();
        commandInfoModel.setId(taskId);

        CommandProcessManager eventWatcherProcess = new CommandProcessManager(watcher, commandInfoModel);

        // 命令线程，到时候还是会改用线程池
        Thread thread = new Thread(() -> {
            try {
                eventWatcherProcess.invokeScheduleWatch();
            } catch (Exception e) {
                logger.info("任务 : " + taskId + " 停止," + e.getMessage());
            } finally {
                // 执行完毕之后
                commandInfoModel.setStatus(-1);
            }
        }, "command-api-" + commandInfoModel.getCommand());

        thread.start();
        commandInfoModel.setStatus(1);
        threadTaskCache.put(taskId, thread);
        commandInfoCache.put(taskId, commandInfoModel);
        logger.info("注册了调度的任务组件:" + commandInfoModel.getCommand() + " -> " + commandInfoModel.getClassNamePattern() + "#" + commandInfoModel.getMethodPattern());
    }

    /**
     * 注册全局任务
     *
     * @param moduleEventWatcher
     * @param commandInfoModel
     */
    public void registerGlobalTask(ModuleEventWatcher moduleEventWatcher, CommandWatcherInfoModel commandInfoModel) throws Exception {
        String taskId = completionCommandInfo(commandInfoModel);
        CommandProcessManager eventWatcherProcess = new CommandProcessManager(moduleEventWatcher, commandInfoModel);
        EventWatcher watcher = eventWatcherProcess.invokeGlobalWatch();
        commandInfoCache.put(taskId, commandInfoModel);
        watcherCache.put(taskId, watcher);
        logger.info("注册了全局的任务组件:" + commandInfoModel.getCommand() + " -> " + commandInfoModel.getClassNamePattern() + "#" + commandInfoModel.getMethodPattern());
    }

    /**
     * 补全纬度
     *
     * @param commandInfoModel
     * @return
     */
    private String completionCommandInfo(CommandWatcherInfoModel commandInfoModel) {
        commandInfoModel.setAppId(SpringApplicationConfig.getInstance().getAppId());
        commandInfoModel.setTaskType(CommandTaskTypeEnums.GLOBAL_TASK.name());
        commandInfoModel.setInvokeNumber(Integer.MAX_VALUE);
        commandInfoModel.setHowManyCount((long) Integer.MAX_VALUE);
        commandInfoModel.setTimeOut(-1L);
        String taskId = getTaskId();
        commandInfoModel.setId(taskId);
        commandInfoModel.setStatus(1);
        return taskId;
    }

    protected String getTaskId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取在线运行命令列表
     *
     * @return
     */
    public List<CommandWatcherInfoModel> list() {
        return commandInfoCache.values().stream().sorted(Comparator.comparingLong(CommandWatcherInfoModel::getRunTime).reversed()).collect(Collectors.toList());
    }

    /**
     * 停止命令任务
     *
     * @param taskId
     */
    public void stop(String taskId) {

        Thread thread = threadTaskCache.get(taskId);

        if (thread != null) {
            thread.interrupt();
        }

        EventWatcher watcher = watcherCache.get(taskId);
        if (watcher != null) {
            watcher.onUnWatched();
        }

        CommandWatcherInfoModel commandInfoModel = commandInfoCache.get(taskId);
        if (commandInfoModel != null) {
            commandInfoModel.setStatus(-1);
        }


        logger.info("命令编号停止: " + taskId);
    }

}
