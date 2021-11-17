package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.module.manager.components.ApplicationConfig;
import com.alibaba.jvm.sandbox.module.manager.components.CommandApiTaskManager;
import com.alibaba.jvm.sandbox.module.manager.components.ParamSupported;
import com.alibaba.jvm.sandbox.module.manager.util.PropertyUtil;
import com.alibaba.jvm.sandbox.module.manager.util.ResponseUtils;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.enums.CommandTaskTypeEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandConfigRequest;
import com.lkx.jvm.sandbox.core.model.command.CommandInfoModel;
import com.lkx.jvm.sandbox.core.model.response.JsonResult;
import com.lkx.jvm.sandbox.core.util.CheckUtils;
import com.lkx.jvm.sandbox.core.util.HttpUtil;
import com.lkx.jvm.sandbox.core.util.JsonUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * 在线命令模块
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 10:11
 */
@MetaInfServices(Module.class)
@Information(id = Constants.MODULE_COMMAND_HTTP_ID, version = "0.0.1", author = "liukaixiong")
public class CommandModule extends ParamSupported implements Module, LoadCompleted {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private final static String CONFIG_COMMAND_URL = PropertyUtil.getWebConfigCommandPath();
    protected CommandApiTaskManager taskManager = new CommandApiTaskManager();
    @Resource
    private ModuleEventWatcher watcher;

    @Override
    public void loadCompleted() {
        CommandConfigRequest request = new CommandConfigRequest();
        request.setApplicationName(ApplicationConfig.getInstance().getApplicationName());
        request.setEnv(ApplicationConfig.getInstance().getEnvironment().toString());
        HttpUtil.Resp resp = HttpUtil.invokePostJson(CONFIG_COMMAND_URL, request);
        int code = resp.getCode();
        if (code == HttpURLConnection.HTTP_OK) {
            // 获取到默认的配置
            JsonResult jsonResult = JsonUtils.parseObject(resp.getBody(), JsonResult.class);
            Object data = jsonResult.getData();

            if (data instanceof JSONArray) {
                logger.info("从客户端拉取该命令类型的配置: " + JsonUtils.toJsonString(data));
                JSONArray configArray = (JSONArray) data;
                for (int i = 0; i < configArray.size(); i++) {
                    CommandInfoModel infoObject = configArray.getObject(i, CommandInfoModel.class);
                    // 纬度补全
                    // 执行全局任务
                    try {
                        taskManager.registerGlobalTask(watcher, infoObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    @Command(Constants.MODULE_COMMAND_HTTP_LIST)
    public void commandList(HttpServletResponse response) throws Exception {
        // 返回在运行的列表
        ResponseUtils.writeJson(response, JsonResult.builder().success(true).data(taskManager.list()).build());
    }

    @Command(Constants.MODULE_COMMAND_HTTP_STOP)
    public void commandStop(Map<String, String> param) {
        final String taskId = getParameter(param, "id");
        taskManager.stop(taskId);
    }

    @Command(Constants.MODULE_COMMAND_HTTP_REGISTER)
    public void commandRegister(Map<String, String> param, HttpServletResponse response) throws Exception {
        logger.info("接收到参数: " + param);
        try {
            CommandInfoModel commandInfoModel = builderCommandInfo(param);
            taskManager.registerScheduleTask(watcher, commandInfoModel);
            ResponseUtils.writeJson(response, JsonResult.builder().success(true).build());
        } catch (Exception e) {
            ResponseUtils.writeJson(response, JsonResult.builder().success(false).message(e.toString()).build());
        }
    }

    protected CommandInfoModel builderCommandInfo(Map<String, String> param) {
        String classNamePattern = param.get("classNamePattern");
        String methodPattern = param.get("methodPattern");
        String command = param.get("command");
        String timeOut = param.getOrDefault("timeOut", "-1");
        String number = param.getOrDefault("invokeNumber", "100");

        CheckUtils.isRequireNotNull(classNamePattern, "classNamePattern");
        CheckUtils.isRequireNotNull(methodPattern, "methodPattern");

        CommandInfoModel commandInfoModel = new CommandInfoModel();
        commandInfoModel.setAppId(ApplicationConfig.getInstance().getAppId());
        commandInfoModel.setCommand(command);
        commandInfoModel.setClassNamePattern(classNamePattern);
        commandInfoModel.setMethodPattern(methodPattern);
        commandInfoModel.setTaskType(CommandTaskTypeEnums.SCHEDULING_TASK.name());
        commandInfoModel.setInvokeNumber(Integer.valueOf(number));
        commandInfoModel.setTimeOut(Long.valueOf(timeOut));

        return commandInfoModel;
    }
}
