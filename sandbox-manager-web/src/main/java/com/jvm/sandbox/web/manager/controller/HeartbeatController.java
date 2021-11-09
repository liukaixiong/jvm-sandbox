package com.jvm.sandbox.web.manager.controller;

import com.alibaba.fastjson.JSON;
import com.jvm.sandbox.web.manager.model.HeartbeatModel;
import com.jvm.sandbox.web.manager.model.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

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
    public static final String HEARTBEAT_PUSH_URL = HEARTBEAT_URL + "/push";

    String result = "{\"mode\":\"ATTACH\",\"environment\":\"test\",\"moduleList\":[{\"id\":\"online-manager-module\",\"isLoaded\":true,\"isActivated\":true,\"version\":\"0.0.2\",\"author\":\"luanjia@taobao.com\"},{\"id\":\"sandbox-info\",\"isLoaded\":true,\"isActivated\":true,\"version\":\"0.0.4\",\"author\":\"luanjia@taobao.com\"},{\"id\":\"sandbox-module-mgr\",\"isLoaded\":true,\"isActivated\":true,\"version\":\"0.0.2\",\"author\":\"luanjia@taobao.com\"},{\"id\":\"sandbox-control\",\"isLoaded\":true,\"isActivated\":true,\"version\":\"0.0.3\",\"author\":\"luanjia@taobao.com\"}],\"port\":\"39651\",\"appName\":\"z-demo\",\"ip\":\"172.19.189.160\",\"isEnableUnsafe\":\"true\",\"namespace\":\"default\",\"pid\":\"27697\",\"version\":\"1.3.3\",\"status\":\"ACTIVE\"}";

    @PostMapping(value = HEARTBEAT_LIST_URL,
            produces = "application/json;charset=UTF-8")
    public JsonResult<List<HeartbeatModel>> list() {
        List<HeartbeatModel> heartbeatModels = Arrays.asList(JSON.parseObject(result, HeartbeatModel.class));
        return JsonResult.builder().success(true).data(heartbeatModels).build();
    }

    @PostMapping(value = HEARTBEAT_PUSH_URL,
            produces = "application/json;charset=UTF-8")
    public JsonResult push(@RequestBody String body) {
        logger.info("接收到的推送消息: " + body);
        return JsonResult.builder().success(true).build();
    }
}
