package com.lkx.jvm.sandbox.core.model.command;

import java.util.Date;

/**
 * 命令日志返回
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/15 - 16:54
 */
public class CommandLogResponse {

    private String id;
    private String message;
    private Date created;
    /**
     * 拓展 链路编号，用来集合日志系统
     */
    private String traceId;
    private Object data;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
