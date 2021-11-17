package com.alibaba.jvm.sandbox.module.manager.process.intercept;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/15 - 21:09
 */
public interface CommandPostProcess {

    default void beforeSend(Object req) {

    }
}
