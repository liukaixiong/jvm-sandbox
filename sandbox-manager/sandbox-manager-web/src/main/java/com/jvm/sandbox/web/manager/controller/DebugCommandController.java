package com.jvm.sandbox.web.manager.controller;

import com.jvm.sandbox.web.manager.model.JsonResult;
import com.jvm.sandbox.web.manager.service.HeartbeatService;
import com.jvm.sandbox.web.manager.utils.ModuleHttpUtils;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.model.command.CommandDebugModel;
import com.lkx.jvm.sandbox.core.util.CheckUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试案例
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/10/26 - 11:40
 */
@RestController
public class DebugCommandController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private HeartbeatService heartbeatService;
    public static final String ROOT_URL = "/debug-command";
    public static final String DEBUG_URL = ROOT_URL + "/debug";


    @PostMapping(value = DEBUG_URL,
            produces = "application/json;charset=UTF-8")
    public JsonResult add(@RequestBody CommandDebugModel request) throws Exception {
        CheckUtils.isRequireNotNull(request.getCommand(), "command");
        CheckUtils.isRequireNotNull(request.getClassNamePattern(), "classNamePattern");

        return ModuleHttpUtils.invoke(heartbeatService, request, Constants.MODULE_COMMAND_DEBUG_HTTP_ID, Constants.MODULE_COMMAND_DEBUG_HTTP_INVOKE);
    }

}
