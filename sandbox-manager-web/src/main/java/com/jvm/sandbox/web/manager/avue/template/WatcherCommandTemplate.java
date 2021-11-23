package com.jvm.sandbox.web.manager.avue.template;

import com.jvm.sandbox.web.manager.avue.AVueConstants;
import com.jvm.sandbox.web.manager.controller.WatcherCommandController;
import com.ruoyi.client.annotation.AVueConfig;
import com.ruoyi.client.annotation.AVueRouteKey;
import com.ruoyi.client.annotation.AVueTableOption;
import com.ruoyi.client.annotation.column.AVueDatetime;
import com.ruoyi.client.annotation.column.AVueInput;

import java.util.Date;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/12 - 14:46
 */
@AVueRouteKey(groupKey = AVueConstants.WATCHER_COMMAND_NAME)
@AVueTableOption(title = "观察命令管理", dialogDrag = true, viewBtn = true, viewBtnText = "查看详情",addBtn = false)
@AVueConfig(list = WatcherCommandController.LIST_URL, save = WatcherCommandController.ADD_URL, update = WatcherCommandController.UPDATE_URL, del = WatcherCommandController.DEL_URL, successField = "success", successKeyword = "true", messageField = "message")
public class WatcherCommandTemplate {
    /**
     * 命令编号
     */
    @AVueInput(prop = "id", label = "命令编号", addDisplay = false, editDisabled = true)
    private String id;

    @AVueInput(prop = "appId", label = "应用编号", addDisplay = false, editDisabled = true)
    private String appId;
    /**
     * 任务类型
     */
    @AVueInput(prop = "taskType", label = "任务类型", addDisplay = false, editDisabled = true)
    private String taskType;
    /**
     * 执行的指令
     */
    @AVueInput(prop = "command", label = "命令类型", required = true, editDisabled = true, span = 24)
    private String command;

    @AVueInput(prop = "classNamePattern", label = "匹配增强类", required = true)
    private String classNamePattern;

    @AVueInput(prop = "methodPattern", label = "匹配增强方法", required = true)
    private String methodPattern;
    /**
     * 执行次数
     */
    @AVueInput(prop = "invokeNumber", label = "执行次数", required = true)
    private Integer invokeNumber;
    /**
     * 超时时间
     */
    @AVueInput(prop = "timeOut", label = "执行时长", required = true)
    private Long timeOut;
    /**
     * 状态
     */
    @AVueInput(prop = "status", label = "执行状态", addDisplay = false)
    private Integer status;
    /**
     * 剩余执行次数
     */
    @AVueInput(prop = "howManyCount", disabled = true, label = "剩余执行次数", addDisplay = false, editDisabled = true)
    private Long howManyCount;

    @AVueDatetime(prop = "runTime", label = "运行时长", format = "hh:mm:ss", row = true, addDisplay = false, editDisabled = true)
    private Long runTime;
    /**
     * 创建时间
     */
    @AVueDatetime(prop = "created", label = "创建时间", format = "yyyy-MM-dd HH:mm:ss", row = true, addDisplay = false, editDisabled = true)
    private Date created = new Date();

}
