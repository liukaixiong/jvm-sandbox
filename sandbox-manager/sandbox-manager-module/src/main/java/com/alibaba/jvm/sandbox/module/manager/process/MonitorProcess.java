package com.alibaba.jvm.sandbox.module.manager.process;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.module.manager.components.AbstractCommandInvoke;
import com.alibaba.jvm.sandbox.module.manager.model.MonitorResult;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * monitor命令：
 * 1. 负责执行一段时间内,该方法的调用次数.
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/2 - 15:16
 */
public class MonitorProcess extends AbstractCommandInvoke {

    private volatile boolean isEnable = true;

    private final Long startTime;

    private final Map<Integer, MonitorResult> monitorResultMap;

    private final ThreadLocal<Long> startTimeLocal = new ThreadLocal<>();

    /**
     * 每个执行对象都对应着一个命令编号
     *
     * @param commandObject 命令配置对象
     */
    public MonitorProcess(CommandWatcherInfoModel commandObject) {
        super(commandObject);
        Long defaultInvokeSecond = ObjectUtils.defaultIfNull(commandObject.getTimeOut(), 120L);
        this.startTime = System.currentTimeMillis();
        this.monitorResultMap = initMonitorResultCache(defaultInvokeSecond);
    }

    private Map<Integer, MonitorResult> initMonitorResultCache(Long invokeSecond) {
        // 按照道理说，只要内部对象安全就行了，该map一旦初始化就不会发生变化，只会不断获取
        Map<Integer, MonitorResult> resultMap = new LinkedHashMap<>(invokeSecond.intValue());
        long start = System.currentTimeMillis();
        for (int i = 0; i < invokeSecond; i++) {
            MonitorResult result = new MonitorResult();
            long nextTime = start + (i + 1) * 1000;
            result.setDateTime(DateFormatUtils.format(nextTime, DateFormatUtils.ISO_DATETIME_FORMAT.getPattern()));
            resultMap.put(i, result);
        }
        return resultMap;
    }

    @Override
    public CommandEnums.Watcher commandName() {
        return CommandEnums.Watcher.monitor;
    }

    @Override
    protected void before(Advice advice) throws Throwable {
        // 判断是否是顶层调用
        if (!advice.isProcessTop()) {
            return;
        }
        startTimeLocal.set(System.currentTimeMillis());
    }


    @Override
    protected void after(Advice advice) throws Throwable {
        // 判断是否是顶层调用
        if (!advice.isProcessTop()) {
            return;
        }
        try {
            long time = System.currentTimeMillis() - startTimeLocal.get();

            int currentSecond = getCurrentSecond();

            MonitorResult result = this.monitorResultMap.get(currentSecond);

            // 如果超出界外
            if (result == null) {
                return;
            }

            if (advice.isThrows()) {
//            if (time % 2 == 0) {
                result.getFail().getAndIncrement();
            } else {
                result.getSuccess().getAndIncrement();
            }

            result.getTotalTime().addAndGet((int) time);

            result.setMaxTime(Math.max(time, result.getMaxTime()));

            result.setMinTime(Math.min(time, result.getMinTime()));

        } finally {
            startTimeLocal.remove();
        }

    }

    private synchronized void stopAndSend(Map<Integer, MonitorResult> monitorResultMap) {
        if (isEnable) {
            isEnable = false;
            sendObjectLog(monitorResultMap);
        }
    }

    @Override
    protected void stop() {
//        System.out.println("结果: " + JsonUtils.toJsonString(this.monitorResultMap));
        stopAndSend(this.monitorResultMap);
    }

    /**
     * 获取当前的秒数，判断当前属于哪一秒中
     *
     * @return
     */
    public int getCurrentSecond() {
        long end = System.currentTimeMillis();
        return (int) Math.floorDiv(Math.floorMod(end, getStartTime()), 1000);
    }

    public Long getStartTime() {
        return startTime;
    }

}
