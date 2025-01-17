package com.jvm.sandbox.web.manager.service;

import com.jvm.sandbox.web.manager.model.HeartbeatModel;

import java.util.List;

/**
 * 心跳管理
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/8-16:59
 */

public interface HeartbeatService {

    /**
     * 获取在线的心跳应用
     *
     * @return
     */
    List<HeartbeatModel> getList();

    /**
     * 注册心跳
     *
     * @param heartbeatModel
     */
    void register(HeartbeatModel heartbeatModel);

    HeartbeatModel getObject(String id);

}
