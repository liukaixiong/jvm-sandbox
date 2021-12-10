package com.alibaba.jvm.sandbox.module.manager.model;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 监控结果内容
 *
 * @author liukaixiong
 * @date 2021/12/2 - 16:05
 */
public class MonitorResult {

    /**
     * 时间
     */
    private String dateTime;

    /**
     * 总数
     */
    private Long total = 0L;

    /**
     * 总耗时
     */
    private AtomicLong totalTime = new AtomicLong(0);

    /**
     * 成功数量
     */
    private AtomicLong success = new AtomicLong(0);

    /**
     * 失败数量
     */
    private AtomicLong fail = new AtomicLong(0);

    /**
     * 平均响应时长
     */
    private Long rt = 0L;

    /**
     * 最大耗时
     */
    private Long maxTime = 0L;
    /**
     * 最小耗时
     */
    private Long minTime = 0L;

    public AtomicLong getTotalTime() {
        return totalTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Long getTotal() {
        return getSuccess().get() + getFail().get();
    }

    public AtomicLong getSuccess() {
        return success;
    }

    public void setSuccess(AtomicLong success) {
        this.success = success;
    }

    public AtomicLong getFail() {
        return fail;
    }

    public void setFail(AtomicLong fail) {
        this.fail = fail;
    }

    public Long getRt() {
        long totalTime = getTotalTime().get();
        Long total = getTotal();
        if (total > 0 && totalTime > 0) {
            return totalTime / total;
        }
        return 0L;
    }

    public Long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Long maxTime) {
        this.maxTime = maxTime;
    }

    public Long getMinTime() {
        return minTime;
    }

    public void setMinTime(Long minTime) {
        this.minTime = minTime;
    }
}
