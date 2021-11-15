package com.jvm.sandbox.web.manager.avue.template;

import com.jvm.sandbox.web.manager.avue.template.node.ModuleInfoTemplate;
import com.jvm.sandbox.web.manager.controller.HeartbeatController;
import com.ruoyi.client.annotation.*;
import com.ruoyi.client.annotation.column.AVueDatetime;
import com.ruoyi.client.annotation.column.AVueDynamic;
import com.ruoyi.client.annotation.column.AVueInput;
import com.ruoyi.client.annotation.column.AVueTable;

import java.util.Date;
import java.util.List;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/8 - 16:56
 */
@AVueRouteKey(groupKey = "sandbox-heartbeat-platform")
@AVueTableOption(title = "沙箱在线应用管理", dialogDrag = true, editBtn = false, delBtn = false, viewBtn = true, viewBtnText = "查看详情")
@AVueConfig(list = HeartbeatController.HEARTBEAT_LIST_URL, successField = "success", successKeyword = "true", messageField = "message")
@AVueEventButtons(
        // 每一行的按钮及事件定义
        tableRowButtons = {
                // 指定方法名称，还有按钮名称
                @AVueClickButton(methodName = "openTabLink", btnName = "查看增强列表", attrExt = {
                        @AVueAttr(name = "group", value = "sandbox-command"),
                        @AVueAttr(name = "query", value = "appId=#{appId}"),
                })
        }
)
public class HeartbeatTemplate {

    @AVueInput(prop = "appId", label = "应用标识")
    private String appId;
    @AVueInput(prop = "appName", label = "应用名称")
    private String appName;
    @AVueInput(prop = "ip", label = "应用ip")
    private String ip;
    @AVueInput(prop = "port", label = "应用端口")
    private String port;
    @AVueInput(prop = "pid", label = "进程号")
    private String pid;
    @AVueInput(prop = "environment", label = "环境")
    private String environment;
    @AVueInput(prop = "mode", label = "启动方式")
    private String mode;
    @AVueInput(prop = "isEnableUnsafe", label = "是否开启安全模式")
    private String isEnableUnsafe;
    @AVueInput(prop = "namespace", label = "工作空间名称")
    private String namespace;
    @AVueInput(prop = "version", label = "版本号", row = true)
    private String version;
    @AVueInput(prop = "status", label = "状态")
    private String status;
    @AVueDatetime(prop = "updateTime", label = "最近更新时间", format = "yyyy-MM-dd HH:mm:ss", row = true)
    private Date updateTime;
    @AVueTable(prop = "moduleList", label = "加载插件", hide = true, width = "1000px", row = true, span = 24)
    private ModuleInfoTemplate moduleList;

}
