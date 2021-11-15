package com.jvm.sandbox.web.manager.controller;

import com.alibaba.fastjson.JSON;
import com.jvm.sandbox.web.manager.model.HeartbeatModel;
import com.jvm.sandbox.web.manager.model.JsonResult;
import com.jvm.sandbox.web.manager.service.HeartbeatService;
import com.lkx.jvm.sandbox.core.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 测试案例
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/10/26 - 11:40
 */
@RestController
public class HeartbeatController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final String HEARTBEAT_URL = "/heartbeat";
    public static final String HEARTBEAT_LIST_URL = HEARTBEAT_URL + "/list";
    public static final String HEARTBEAT_PUSH_URL = Constants.DEFAULT_CONFIG_WEB_HEARTBEAT_VALUE;


    @Autowired
    private HeartbeatService heartbeatService;

    @PostMapping(value = HEARTBEAT_LIST_URL,
            produces = "application/json;charset=UTF-8")
    public JsonResult<List<HeartbeatModel>> list() {

        return JsonResult.builder().success(true).data(heartbeatService.getList()).build();
    }

    @PostMapping(value = HEARTBEAT_PUSH_URL,
            produces = "application/json;charset=UTF-8")
    public JsonResult push(@RequestBody HeartbeatModel model) {
        logger.info("接收到的推送消息: " + model);
        heartbeatService.register(model);
        return JsonResult.builder().success(true).build();
    }


}
