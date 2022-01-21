package com.sandbox.demo.model;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/21 - 14:02
 */
public class RS {

    private boolean success = true;
    private Object obj;
    private String message;
    private String traceId;

    public RS() {
    }

    public RS(Object obj) {
        this.success = true;
        this.obj = obj;
    }

    public RS(String message) {
        this.success = false;
        this.message = message;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
