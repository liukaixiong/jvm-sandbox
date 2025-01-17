package com.alibaba.jvm.sandbox.module.manager.debug;

import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.enums.CommandTaskTypeEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandDebugModel;
import com.lkx.jvm.sandbox.core.model.response.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/22 - 11:14
 */
public interface CommandDebugProcess<T> {
    Logger logger = LoggerFactory.getLogger(CommandDebugProcess.class);

    /**
     * 命令信息
     *
     * @return
     */
    CommandEnums.Debug command();

    /**
     * 执行信息
     *
     * @param req
     * @return
     */
    T invoke(Instrumentation inst, CommandDebugModel req) throws Exception;


}
