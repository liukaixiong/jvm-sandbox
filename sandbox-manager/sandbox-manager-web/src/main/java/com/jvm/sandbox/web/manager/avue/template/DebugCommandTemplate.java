package com.jvm.sandbox.web.manager.avue.template;

import com.jvm.sandbox.web.manager.avue.AVueConstants;
import com.jvm.sandbox.web.manager.controller.WatcherCommandController;
import com.ruoyi.client.annotation.AVueConfig;
import com.ruoyi.client.annotation.AVueRouteKey;
import com.ruoyi.client.annotation.AVueTableOption;
import com.ruoyi.client.annotation.column.AVueDatetime;
import com.ruoyi.client.annotation.column.AVueInput;
import com.ruoyi.client.annotation.column.AVueJson;
import com.ruoyi.client.annotation.column.AVueSelect;

import java.util.Date;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/12 - 14:46
 */
@AVueRouteKey(groupKey = AVueConstants.DEBUG_COMMAND_NAME)
@AVueTableOption(title = "应用调试模块", dialogDrag = true, viewBtn = true, viewBtnText = "查看详情")
@AVueConfig(list = WatcherCommandController.LIST_URL, save = WatcherCommandController.ADD_URL, update = WatcherCommandController.UPDATE_URL, del = WatcherCommandController.DEL_URL, successField = "success", successKeyword = "true", messageField = "message")
public class DebugCommandTemplate {

    @AVueInput(prop = "appId", label = "应用编号", readonly = true)
    private String appId;

    @AVueSelect(prop = "command", label = "命令类型", required = true, dicData = "Debug", span = 24)
    private String command;

    @AVueInput(prop = "classNamePattern", label = "匹配增强类", required = true)
    private String classNamePattern;

    @AVueInput(prop = "methodPattern", label = "匹配增强方法", required = true)
    private String methodPattern;

//    @AVueJson(prop = "resultLogShow", label = "结果展示",span = 24)
//    private String resultLogShow;

}
