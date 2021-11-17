package com.jvm.sandbox.web.manager.controller;

import com.alibaba.fastjson.JSON;
import com.jvm.sandbox.web.manager.model.HeartbeatModel;
import com.jvm.sandbox.web.manager.model.JsonResult;
import com.jvm.sandbox.web.manager.service.HeartbeatService;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.model.command.CommandConfigRequest;
import com.lkx.jvm.sandbox.core.model.command.CommandInfoModel;
import com.lkx.jvm.sandbox.core.model.command.CommandLogResponse;
import com.lkx.jvm.sandbox.core.util.CheckUtils;
import com.lkx.jvm.sandbox.core.util.HttpUtil;
import com.lkx.jvm.sandbox.core.util.JsonUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
    public static final String UPDATE_URL = ROOT_URL + "/update";
    public static final String DEL_URL = ROOT_URL + "/delete";
    public static final String PUSH_URL = ROOT_URL + "/push";


    @Autowired
    private HeartbeatService heartbeatService;

    @PostMapping(value = LIST_URL,
            produces = "application/json;charset=UTF-8")
    public JsonResult<List<CommandInfoModel>> list(@RequestBody CommandInfoModel infoModel) throws Exception {
        CheckUtils.isRequireNotNull(infoModel.getAppId(), "appId");

        HeartbeatModel heartbeatModel = heartbeatService.getObject(infoModel.getAppId());

        List<CommandInfoModel> commandInfoModelList = new ArrayList<>();

        if (heartbeatModel == null) {
            return JsonResult.builder().success(true).data(commandInfoModelList).build();
        }

        JsonResult invoke = invoke(infoModel, Constants.MODULE_COMMAND_HTTP_LIST);

        if (!invoke.isSuccess()) {
            return JsonResult.builder().success(true).data(commandInfoModelList).build();
        }

        return invoke;
    }


    @PostMapping(value = ADD_URL,
            produces = "application/json;charset=UTF-8")
    public JsonResult add(@RequestBody CommandInfoModel request) throws Exception {
        CheckUtils.isRequireNotNull(request.getAppId(), "appId");
        CheckUtils.isRequireNotNull(request.getClassNamePattern(), "classNamePattern");
        CheckUtils.isRequireNotNull(request.getCommand(), "command");
        CheckUtils.isRequireNotNull(request.getMethodPattern(), "methodPattern");

        return invoke(request, Constants.MODULE_COMMAND_HTTP_REGISTER);
    }

    @PostMapping(value = DEL_URL,
            produces = "application/json;charset=UTF-8")
    public JsonResult del(@RequestBody CommandInfoModel request) throws Exception {
        CheckUtils.isRequireNotNull(request.getAppId(), "appId");
        CheckUtils.isRequireNotNull(request.getId(), "id");

        return invoke(request, Constants.MODULE_COMMAND_HTTP_STOP);
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

    /**
     * 命令执行日志推送接收
     *
     * @param model
     * @return
     */
    @PostMapping(value = Constants.DEFAULT_CONFIG_WEB_COMMAND_PUSH_LOG_VALUE,
            produces = "application/json;charset=UTF-8")
    public JsonResult configCommandLogPush(@RequestBody CommandLogResponse model) {
        logger.info("接收到监听的命令事件: " + JsonUtils.toJsonString(model));
        return JsonResult.builder().success(true).build();
    }

    /**
     * 执行远程处理
     *
     * @param infoModel
     * @param routerPath
     * @return
     */
    private JsonResult invoke(CommandInfoModel infoModel, String routerPath) throws Exception {
        // 获取应用信息
        HeartbeatModel projectInfo = heartbeatService.getObject(infoModel.getAppId());
        if (projectInfo != null) {
            // 构建远程请求
            String requestUrl = getRequestUrl(projectInfo, routerPath);
            // 执行远程调用
            return invokeHttp(requestUrl, infoModel);
        }
        return JsonResult.builder().success(false).message("操作失败").build();
    }

    /**
     * 执行远程调用
     *
     * @param url       请求路径
     * @param infoModel 请求参数
     * @return
     */
    private JsonResult invokeHttp(String url, CommandInfoModel infoModel) throws Exception {

        Map<String, String> requestMap = BeanUtils.describe(infoModel);

        HttpUtil.Resp resp = HttpUtil.doPost(url, requestMap);

        if (resp.isSuccess()) {
            return JsonUtils.parseObject(resp.getBody(), JsonResult.class);
        }
        return JsonResult.builder().success(false).message(resp.getMessage()).build();
    }

    /**
     * 获取执行远程调用
     *
     * @param heartbeatModel
     * @param routePath
     * @return
     */
    private String getRequestUrl(HeartbeatModel heartbeatModel, String routePath) {
        String ip = heartbeatModel.getIp();
        String port = heartbeatModel.getPort();
        String defaultSandboxPath = Constants.DEFAULT_SANDBOX_PATH;

//        String url = "http://" + ip + ":" + port + defaultSandboxPath + "/" + Constants.MODULE_COMMAND_HTTP_ID + "/" + Constants.MODULE_COMMAND_HTTP_LIST;
        String url = "http://127.0.0.1:" + port + defaultSandboxPath + Constants.MODULE_COMMAND_HTTP_ID + "/" + routePath;
        return url;
    }

    public static void main(String[] args) throws Exception {
        String json = "{\"appId\":\"unknown-c0a802c0\",\"classNamePattern\":\"2\",\"command\":\"1\",\"created\":1636947675160,\"id\":\"\",\"invokeNumber\":4,\"methodPattern\":\"3\",\"runTime\":7351539,\"taskType\":\"\",\"timeOut\":5}";


        CommandInfoModel commandInfoModel = JSON.parseObject(json, CommandInfoModel.class);

        Map<String, String> map = new LinkedHashMap<>();
        BeanUtils.populate(commandInfoModel, map);
        System.out.println(map);

    }
}
