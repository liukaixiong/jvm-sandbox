package com.lkx.jvm.sandbox.core.model.command;

import java.util.Date;

/**
 * 命令观察对象
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 11:40
 */
public class CommandWatcherInfoModel extends CommandModel {

    /**
     * 命令编号
     */
    private String id;

    /**
     * 应用唯一标识
     */
    String appId;

    /**
     * 任务类型
     */
    private String taskType;

    /**
     * 执行次数
     */
    private Integer invokeNumber;
    /**
     * 超时时间
     */
    private Long timeOut;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 剩余执行次数
     */
    private Long howManyCount;

    private Long runTime;

    /**
     * 创建时间
     */
    private Date created = new Date();

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setRunTime(Long runTime) {
        this.runTime = runTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    /**
     * 运行时长
     *
     * @return
     */
    public Long getRunTime() {
        if (status != null && status != -1) {
            return System.currentTimeMillis() - created.getTime();
        } else {
            return this.runTime;
        }
    }

    public Integer getInvokeNumber() {
        return invokeNumber;
    }

    public void setInvokeNumber(Integer invokeNumber) {
        this.invokeNumber = invokeNumber;
    }

    public Long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
    }

    public Long getHowManyCount() {
        return howManyCount;
    }

    public void setHowManyCount(Long howManyCount) {
        this.howManyCount = howManyCount;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
