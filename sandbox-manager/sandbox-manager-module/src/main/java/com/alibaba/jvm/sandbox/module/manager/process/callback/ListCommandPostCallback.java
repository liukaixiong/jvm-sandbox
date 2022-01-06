package com.alibaba.jvm.sandbox.module.manager.process.callback;

import com.lkx.jvm.sandbox.core.factory.GlobalFactoryHelper;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 集合类型的回调
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/17 - 19:21
 */
@Component
public class ListCommandPostCallback implements CommandPostCallback {

    private CommandPostCallback callback;

    @Override
    public void callback(CommandWatcherInfoModel commandInfoModel, Object req) {
        List<CommandPostCallback> callbacks = GlobalFactoryHelper.getInstance().getList(CommandPostCallback.class);
        for (int i = 0; i < callbacks.size(); i++) {
            callbacks.get(i).callback(commandInfoModel, req);
        }

        callback.callback(commandInfoModel, req);
    }

    public void setCallback(CommandPostCallback callback) {
        this.callback = callback;
    }
}
