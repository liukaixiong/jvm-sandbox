package com.lkx.jvm.sandbox.core.model.command;

import java.util.Date;
import java.util.Map;

/**
 * 命令执行详情返回结果
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 11:40
 */
public class CommandInfoModel {

    /**
     * 命令编号
     */
    private String id;

    /**
     * 应用唯一标识
     */
    String appId;
    /**
     * 执行的指令
     */
    private String command;
    /**
     * 任务类型
     */
    private String taskType;

    private String classNamePattern;

    private String methodPattern;
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

    /**
     * 拓展参数
     */
    private Map<String, Object> extData;

    private Long runTime;

    /**
     * 创建时间
     */
    private Date created = new Date();

    public Map<String, Object> getExtData() {
        return extData;
    }

    public void setExtData(Map<String, Object> extData) {
        this.extData = extData;
    }

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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getClassNamePattern() {
        return classNamePattern;
    }

    public void setClassNamePattern(String classNamePattern) {
        this.classNamePattern = classNamePattern;
    }

    public String getMethodPattern() {
        return methodPattern;
    }

    public void setMethodPattern(String methodPattern) {
        this.methodPattern = methodPattern;
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
