package com.jvm.sandbox.web.manager.controller;

import com.jvm.sandbox.web.manager.model.HeartbeatModel;
import com.jvm.sandbox.web.manager.model.JsonResult;
import com.jvm.sandbox.web.manager.service.HeartbeatService;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.model.command.CommandConfigRequest;
import com.lkx.jvm.sandbox.core.model.command.CommandInfoModel;
import com.lkx.jvm.sandbox.core.util.CheckUtils;
import com.lkx.jvm.sandbox.core.util.HttpUtil;
import com.lkx.jvm.sandbox.core.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试案例
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/10/26 - 11:40
 */
@RestController
public class CommandController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final String ROOT_URL = "/command";
    public static final String LIST_URL = ROOT_URL + "/list";
    public static final String ADD_URL = ROOT_URL + "/add";
    public static final String PUSH_URL = ROOT_URL + "/push";


    @Autowired
    private HeartbeatService heartbeatService;

    @PostMapping(value = LIST_URL,
            produces = "application/json;charset=UTF-8")
    public JsonResult<List<CommandInfoModel>> list(@RequestBody CommandInfoModel infoModel) {
        CheckUtils.isRequireNotNull(infoModel.getAppId(), "appId");

        HeartbeatModel heartbeatModel = heartbeatService.getObject(infoModel.getAppId());

        List<CommandInfoModel> commandInfoModelList = new ArrayList<>();

        if (heartbeatModel == null) {
            return JsonResult.builder().success(true).data(commandInfoModelList).build();
        }

        String ip = heartbeatModel.getIp();
        String port = heartbeatModel.getPort();
        String defaultSandboxPath = Constants.DEFAULT_SANDBOX_PATH;

//        String url = "http://" + ip + ":" + port + defaultSandboxPath + "/" + Constants.MODULE_COMMAND_HTTP_ID + "/" + Constants.MODULE_COMMAND_HTTP_LIST;
        String url = "http://127.0.0.1:" + port + defaultSandboxPath + Constants.MODULE_COMMAND_HTTP_ID + "/" + Constants.MODULE_COMMAND_HTTP_LIST;

        Map<String, String> requestMap = new HashMap<>();
//        try {
//            BeanUtils.populate(heartbeatModel, requestMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        HttpUtil.Resp resp = HttpUtil.doPost(url, requestMap);

        if (resp.isSuccess()) {
            JsonResult jsonResult = JsonUtils.parseObject(resp.getBody(), JsonResult.class);
            return jsonResult;
        }

        return JsonResult.builder().success(true).data(commandInfoModelList).build();
    }

    @PostMapping(value = ADD_URL,
            produces = "application/json;charset=UTF-8")
    public JsonResult add(CommandInfoModel request) {
        return JsonResult.builder().success(true).data(null).build();
    }

    /**
     * 获取应用的命令全局增强配置
     *
     * @param model
     * @return
     */
    @PostMapping(value = Constants.DEFAULT_CONFIG_WEB_COMMAND_CONFIG_VALUE,
            produces = "application/json;charset=UTF-8")
    public JsonResult configCommand(@RequestBody CommandConfigRequest model) {
        // 这部分数据需要存储到数据库
        List<CommandInfoModel> commandInfoModelList = new ArrayList<>();
        CommandInfoModel commandInfoModel = new CommandInfoModel();
        commandInfoModel.setClassNamePattern("java.lang.System");
        commandInfoModel.setMethodPattern("gc");
        commandInfoModel.setCommand("stack");
        commandInfoModelList.add(commandInfoModel);
        logger.info("返回默认配置:" + JsonUtils.toJsonString(commandInfoModelList));
        return JsonResult.builder().success(true).data(commandInfoModelList).build();
    }


}
