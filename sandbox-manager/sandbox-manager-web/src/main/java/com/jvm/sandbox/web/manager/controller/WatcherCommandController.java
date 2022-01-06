package com.jvm.sandbox.web.manager.controller;

import com.jvm.sandbox.web.manager.model.JsonResult;
import com.jvm.sandbox.web.manager.service.HeartbeatService;
import com.jvm.sandbox.web.manager.utils.ModuleHttpUtils;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.model.command.CommandConfigRequest;
import com.lkx.jvm.sandbox.core.model.command.CommandLogResponse;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;
import com.lkx.jvm.sandbox.core.util.CheckUtils;
import com.lkx.jvm.sandbox.core.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试案例
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/10/26 - 11:40
 */
@RestController
public class WatcherCommandController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
    public JsonResult<List<CommandWatcherInfoModel>> list(@RequestBody CommandWatcherInfoModel infoModel) throws Exception {
        CheckUtils.isRequireNotNull(infoModel.getAppId(), "appId");

        JsonResult invoke = ModuleHttpUtils.invoke(heartbeatService, infoModel, Constants.MODULE_COMMAND_WATCHER_HTTP_ID, Constants.MODULE_COMMAND_HTTP_LIST);

        if (!invoke.isSuccess()) {
            return JsonResult.builder().success(true).data(new ArrayList<>()).build();
        }

        return invoke;
    }


    @PostMapping(value = ADD_URL,
            produces = "application/json;charset=UTF-8")
    public JsonResult add(@RequestBody CommandWatcherInfoModel request) throws Exception {
        CheckUtils.isRequireNotNull(request.getAppId(), "appId");
        CheckUtils.isRequireNotNull(request.getClassNamePattern(), "classNamePattern");
        CheckUtils.isRequireNotNull(request.getCommand(), "command");
        CheckUtils.isRequireNotNull(request.getMethodPattern(), "methodPattern");
        return ModuleHttpUtils.invoke(heartbeatService, request,Constants.MODULE_COMMAND_WATCHER_HTTP_ID, Constants.MODULE_COMMAND_HTTP_REGISTER);
    }

    @PostMapping(value = DEL_URL,
            produces = "application/json;charset=UTF-8")
    public JsonResult del(@RequestBody CommandWatcherInfoModel request) throws Exception {
        CheckUtils.isRequireNotNull(request.getAppId(), "appId");
        CheckUtils.isRequireNotNull(request.getId(), "id");
        return ModuleHttpUtils.invoke(heartbeatService, request, Constants.MODULE_COMMAND_WATCHER_HTTP_ID,Constants.MODULE_COMMAND_HTTP_STOP);
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
        List<CommandWatcherInfoModel> commandInfoModelList = new ArrayList<>();
        CommandWatcherInfoModel commandInfoModel = new CommandWatcherInfoModel();
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

//    /**
//     * 执行远程处理
//     *
//     * @param infoModel
//     * @param routerPath
//     * @return
//     */
//    private JsonResult invoke(CommandWatcherInfoModel infoModel, String routerPath) throws Exception {
//        // 获取应用信息
//        HeartbeatModel projectInfo = heartbeatService.getObject(infoModel.getAppId());
//        if (projectInfo != null) {
//            // 构建远程请求
//            String requestUrl = getRequestUrl(projectInfo, routerPath);
//            // 执行远程调用
//            return invokeHttp(requestUrl, infoModel);
//        }
//        return JsonResult.builder().success(false).message("操作失败").build();
//    }
//
//    /**
//     * 执行远程调用
//     *
//     * @param url       请求路径
//     * @param infoModel 请求参数
//     * @return
//     */
//    private JsonResult invokeHttp(String url, CommandWatcherInfoModel infoModel) throws Exception {
//
//        Map<String, String> requestMap = BeanUtils.describe(infoModel);
//
//        HttpUtil.Resp resp = HttpUtil.doPost(url, requestMap);
//
//        if (resp.isSuccess()) {
//            return JsonUtils.parseObject(resp.getBody(), JsonResult.class);
//        }
//        return JsonResult.builder().success(false).message(resp.getMessage()).build();
//    }
//
//    /**
//     * 获取执行远程调用
//     *
//     * @param heartbeatModel
//     * @param routePath
//     * @return
//     */
//    private String getRequestUrl(HeartbeatModel heartbeatModel, String routePath) {
//        String ip = heartbeatModel.getIp();
//        String port = heartbeatModel.getPort();
//        String defaultSandboxPath = Constants.DEFAULT_SANDBOX_PATH;
//
////        String url = "http://" + ip + ":" + port + defaultSandboxPath + "/" + Constants.MODULE_COMMAND_HTTP_ID + "/" + Constants.MODULE_COMMAND_HTTP_LIST;
//        String url = "http://127.0.0.1:" + port + defaultSandboxPath + Constants.MODULE_COMMAND_WATCHER_HTTP_ID + "/" + routePath;
//        return url;
//    }

}
