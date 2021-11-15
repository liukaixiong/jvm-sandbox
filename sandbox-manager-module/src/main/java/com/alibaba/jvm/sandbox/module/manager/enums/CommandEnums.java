package com.alibaba.jvm.sandbox.module.manager.enums;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 11:23
 */
public enum CommandEnums {

    TRACE("查询方法的执行链路信息"),
    STACK("查找被调用方法上层方法栈");

    private String describe;

    CommandEnums(String describe) {
        this.describe = describe;
    }
}
