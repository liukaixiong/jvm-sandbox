package com.jvm.sandbox.web.manager.service.impl;

import com.jvm.sandbox.web.manager.model.HeartbeatModel;
import com.jvm.sandbox.web.manager.service.HeartbeatService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 基于内存的心跳存储
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/8 - 17:07
 */
@Component
public class MemoryHeartbeatServiceImpl implements HeartbeatService {

    @Override
    public List<HeartbeatModel> getList() {
        return null;
    }

    @Override
    public void register(HeartbeatModel heartbeatModel) {

    }

}
