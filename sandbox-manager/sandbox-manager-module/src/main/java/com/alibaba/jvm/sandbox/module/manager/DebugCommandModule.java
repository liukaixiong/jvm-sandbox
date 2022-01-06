package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.module.manager.debug.CommandDebugProcess;
import com.alibaba.jvm.sandbox.module.manager.util.ResponseUtils;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.factory.GlobalFactoryHelper;
import com.lkx.jvm.sandbox.core.model.command.CommandDebugModel;
import com.lkx.jvm.sandbox.core.model.response.JsonResult;
import com.lkx.jvm.sandbox.core.util.CheckUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Map;

/**
 * 负责执行调试命令的模块
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/22 - 11:04
 */
@MetaInfServices(Module.class)
@Information(id = Constants.MODULE_COMMAND_DEBUG_HTTP_ID, version = "0.0.1", author = "liukaixiong")
public class DebugCommandModule implements Module, LoadCompleted {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private Instrumentation instrumentation;

    @Override
    public void loadCompleted() {

    }

    @Command(Constants.MODULE_COMMAND_DEBUG_HTTP_INVOKE)
    public void commandRegister(Map<String, String> param, HttpServletResponse response) throws Exception {
        logger.info("接收到参数: " + param);
        try {
            CommandDebugModel debugModel = builderCommandDebugInfo(param);
            List<CommandDebugProcess> commandDebugProcessList = GlobalFactoryHelper.getInstance().getList(CommandDebugProcess.class);
            for (CommandDebugProcess debugProcess : commandDebugProcessList) {
                if (debugProcess.command().name().equals(debugModel.getCommand())) {
                    Object invoke = debugProcess.invoke(instrumentation, debugModel);
                    ResponseUtils.writeJson(response, JsonResult.builder().success(true).data(invoke).build());
                    break;
                }
            }
        } catch (Exception e) {
            ResponseUtils.writeJson(response, JsonResult.builder().success(false).message(e.toString()).build());
        }
    }

    private CommandDebugModel builderCommandDebugInfo(Map<String, String> param) {

        String command = param.get("command");
        String classNamePattern = param.get("classNamePattern");
        String methodPattern = param.get("methodPattern");

        CheckUtils.isRequireNotNull(command, "command");


        CommandDebugModel debugModel = new CommandDebugModel();
        debugModel.setCommand(command);
        debugModel.setClassNamePattern(classNamePattern);
        debugModel.setMethodPattern(methodPattern);

        return debugModel;
    }
}
