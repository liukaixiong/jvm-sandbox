package com.alibaba.jvm.sandbox.module.manager.process.callback;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.module.manager.util.PropertyUtil;
import com.lkx.jvm.sandbox.core.model.command.CommandInfoModel;
import com.lkx.jvm.sandbox.core.model.command.CommandLogResponse;
import com.lkx.jvm.sandbox.core.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 命令日志推送
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/17 - 18:43
 */
public class HttpCommandLogSendCallback implements CommandPostCallback {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String commandLogPushUrl = PropertyUtil.getWebConfigCommandLogPushPath();

    @Override
    public void callback(CommandInfoModel commandInfoModel, Object req) {
        CommandLogResponse response = new CommandLogResponse();
        response.setId(commandInfoModel.getId());
        response.setData(req);
        response.setCreated(new Date());
        HttpUtil.Resp resp = HttpUtil.invokePostJson(commandLogPushUrl, null, response);
        if (!resp.isSuccess()) {
            logger.error("远程命令执行日志推送失败" + req + "\t" + resp.getMessage());
        }
    }
}
