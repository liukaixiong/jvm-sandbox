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
    PRINT_LOG("关键日志"),

    CAT_METHOD_TRANSACTION_POINT("CAT方法事务埋点"),

    CAT_URL_TRANSACTION_POINT("CAT请求事务埋点"),

    CAT_MYBATIS_TRANSACTION_POINT("CAT_Mybatis埋点"),

    TRACE_ID("日志链路编号"),

    CAT_EVENT_POINT("CAT事件埋点");

    private final String describe;

    AdviceNameDefinition(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }


}
