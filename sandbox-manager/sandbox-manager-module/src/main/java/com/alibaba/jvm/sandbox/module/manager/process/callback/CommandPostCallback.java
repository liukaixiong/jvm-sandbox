package com.alibaba.jvm.sandbox.module.manager.process.callback;

import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/15 - 21:09
 */
public interface CommandPostCallback {

    default void callback(CommandWatcherInfoModel commandInfoModel, Object req) {

    }
}
