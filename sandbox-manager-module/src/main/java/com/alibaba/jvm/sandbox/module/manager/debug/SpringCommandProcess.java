package com.alibaba.jvm.sandbox.module.manager.debug;

import com.alibaba.jvm.sandbox.module.manager.components.SpringContextContainer;
import com.alibaba.jvm.sandbox.module.manager.util.DebugMethodUtils;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandDebugModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

/**
 * spring相关的执行命令
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/22 - 16:17
 */
public class SpringCommandProcess implements CommandDebugProcess<Object> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CommandEnums.Debug command() {
        return CommandEnums.Debug.getBean;
    }

    @Override
    public Object invoke(Instrumentation inst, CommandDebugModel req) {
        String classNamePattern = req.getClassNamePattern();
        try {
            Object bean = null;
            if (classNamePattern.contains(".")) {
                Class<?> clazz = Class.forName(classNamePattern);
                bean = SpringContextContainer.getInstance().getBean(clazz);
            } else {
                bean = SpringContextContainer.getInstance().getBean(classNamePattern);
            }
            return DebugMethodUtils.getInvokeResult(req.getMethodPattern(), bean);
        } catch (Exception e) {
            logger.error("getBean error", e);
            return e.getMessage();
        }
    }
}
