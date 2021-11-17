package com.alibaba.jvm.sandbox.module.manager.process;

import com.alibaba.jvm.sandbox.module.manager.components.AbstractCommandInvoke;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandInfoModel;

/**
 * 查看对应的方法耗时情况.
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/15 - 17:12
 */
public class TimeConsumingProcess extends AbstractCommandInvoke {

    /**
     * 每个执行对象都对应着一个命令编号
     *
     * @param commandObject
     */
    public TimeConsumingProcess(CommandInfoModel commandObject) {
        super(commandObject);
    }

    @Override
    public CommandEnums commandName() {
        return CommandEnums.time;
    }

}
