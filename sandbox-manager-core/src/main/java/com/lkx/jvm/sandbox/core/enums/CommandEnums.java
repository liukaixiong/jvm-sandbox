package com.lkx.jvm.sandbox.core.enums;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 11:23
 */
public enum CommandEnums {
    ;

    public enum Watcher {
        trace("查询方法的执行链路信息"),
        time("查询方法的耗费时长"),
        log("通过日志装饰方法"),
        monitor("一段时间内的方法执行监控"),
        method_trace_result("记录方法执行结果"),
        stack("查找被调用方法上层方法栈");

        private String describe;

        Watcher(String describe) {
            this.describe = describe;
        }
    }

    public enum Debug {
        jad("实时反编译Class文件"),
        getBean("获取Spring中的bean对象"),
        getStatic("获取类的静态属性");
        private String describe;

        Debug(String describe) {
            this.describe = describe;
        }
    }
}
