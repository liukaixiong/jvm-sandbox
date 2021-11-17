package com.lkx.jvm.sandbox.core.enums;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 11:23
 */
public enum CommandEnums {

    trace("查询方法的执行链路信息"),
    time("查询方法的耗费时长"),
    log("通过日志装饰方法"),
    stack("查找被调用方法上层方法栈");

    private String describe;

    CommandEnums(String describe) {
        this.describe = describe;
    }
}
