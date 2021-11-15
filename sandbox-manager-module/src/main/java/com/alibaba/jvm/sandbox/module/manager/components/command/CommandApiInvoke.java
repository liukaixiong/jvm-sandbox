package com.alibaba.jvm.sandbox.module.manager.components.command;

import com.alibaba.jvm.sandbox.module.manager.enums.CommandEnums;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 11:22
 */
public interface CommandApiInvoke {

    /**
     * 处理的命令名称
     *
     * @return
     */
    public CommandEnums commandName();

}
