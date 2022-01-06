package com.jvm.sandbox.web.manager.avue.template.node;

import com.jvm.sandbox.web.manager.controller.HeartbeatController;
import com.ruoyi.client.annotation.AVueAttr;
import com.ruoyi.client.annotation.AVueClickButton;
import com.ruoyi.client.annotation.AVueEventButtons;
import com.ruoyi.client.annotation.AVueTableOption;
import com.ruoyi.client.annotation.column.AVueInput;
import com.ruoyi.client.annotation.column.AVueSwitch;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/8 - 17:22
 */
@AVueTableOption(width = "1000px", span = 24, addBtn = false, delBtn = false, editBtn = false)
// 设置后台接口调用之后成功或者失败的结构模型
@AVueEventButtons(
        // 每一行的按钮及事件定义
        tableRowButtons = {
                // 指定方法名称，还有按钮名称
                @AVueClickButton(methodName = "confirmClickRemoteApi", btnName = "禁用", attrExt = {
                        @AVueAttr(name = "title", value = "是否下线?"),
                        @AVueAttr(name = "url", value = HeartbeatController.HEARTBEAT_PUSH_URL),
                        @AVueAttr(name = "method", value = "post"),
                })
        }
)
public class ModuleInfoTemplate {
    @AVueInput(prop = "id", label = "插件编号", row = true)
    private String id;
    @AVueSwitch(prop = "isLoaded", label = "是否加载", row = true)
    private boolean isLoaded;
    @AVueSwitch(prop = "isActivated", label = "是否开启", row = true)
    private boolean isActivated;
    @AVueInput(prop = "version", label = "版本号", row = true)
    private String version;
    @AVueInput(prop = "author", label = "作者", row = true)
    private String author;
}
