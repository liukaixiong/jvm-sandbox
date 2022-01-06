package com.sandbox.manager.api;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/13 - 15:01
 */
public enum AdviceNameDefinition {

    /**
     * 统计耗时情况
     */
    DURATION("统计耗时"),
    /**
     * 链路信息
     */
    TRACE_INFO("链路追踪"),
    /**
     * 异常信息
     */
    ERROR_INFO("异常信息"),
    /**
     * 植入日志
     */
    PRINT_LOG("关键日志");

    private final String describe;

    AdviceNameDefinition(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }
}
