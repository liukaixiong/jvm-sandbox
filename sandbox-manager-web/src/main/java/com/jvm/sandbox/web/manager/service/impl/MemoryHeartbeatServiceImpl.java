package com.jvm.sandbox.web.manager.service.impl;

import com.jvm.sandbox.web.manager.model.HeartbeatModel;
import com.jvm.sandbox.web.manager.service.HeartbeatService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于内存的心跳存储
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/8 - 17:07
 */
@Component
public class MemoryHeartbeatServiceImpl implements HeartbeatService {
    private Map<String, HeartbeatModel> heartbeatCache = new ConcurrentHashMap<>();

    @Override
    public List<HeartbeatModel> getList() {
        return new ArrayList<>(heartbeatCache.values());
    }

    @Override
    public void register(HeartbeatModel heartbeatModel) {
        heartbeatCache.put(heartbeatModel.getAppId(), heartbeatModel);
    }

    @Override
    public HeartbeatModel getObject(String id) {
        return heartbeatCache.get(id);
    }
}
