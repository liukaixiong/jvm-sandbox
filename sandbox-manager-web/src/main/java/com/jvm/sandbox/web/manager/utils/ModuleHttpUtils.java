package com.jvm.sandbox.web.manager.utils;

import com.jvm.sandbox.web.manager.model.HeartbeatModel;
import com.jvm.sandbox.web.manager.model.JsonResult;
import com.jvm.sandbox.web.manager.service.HeartbeatService;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.model.command.CommandModel;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;
import com.lkx.jvm.sandbox.core.util.HttpUtil;
import com.lkx.jvm.sandbox.core.util.JsonUtils;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/22 - 17:16
 */
public class ModuleHttpUtils {
    /**
     * 执行远程处理
     *
     * @param heartbeatService 应用信息
     * @param infoModel        执行信息
     * @param modulePath       模块路径
     * @param routerPath       命令路径
     * @return
     */
    public static JsonResult invoke(HeartbeatService heartbeatService, CommandModel infoModel, String modulePath, String routerPath) throws Exception {
        HeartbeatModel heartbeatModel = heartbeatService.getObject(infoModel.getAppId());

        // 获取应用信息
        if (heartbeatModel != null) {
            // 构建远程请求
            String requestUrl = getRequestUrl(heartbeatModel, modulePath, routerPath);
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
    private static JsonResult invokeHttp(String url, CommandModel infoModel) throws Exception {

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
    private static String getRequestUrl(HeartbeatModel heartbeatModel, String modulePath, String routePath) {
        String ip = heartbeatModel.getIp();
        String port = heartbeatModel.getPort();
        String defaultSandboxPath = Constants.DEFAULT_SANDBOX_PATH;

//        String url = "http://" + ip + ":" + port + defaultSandboxPath + "/" + Constants.MODULE_COMMAND_HTTP_ID + "/" + Constants.MODULE_COMMAND_HTTP_LIST;
        String url = "http://127.0.0.1:" + port + defaultSandboxPath + modulePath + "/" + routePath;
        return url;
    }
}
