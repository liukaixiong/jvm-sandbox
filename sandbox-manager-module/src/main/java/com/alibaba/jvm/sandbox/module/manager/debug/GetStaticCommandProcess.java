package com.alibaba.jvm.sandbox.module.manager.debug;

import com.alibaba.jvm.sandbox.module.manager.util.DebugMethodUtils;
import com.google.common.base.Splitter;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandDebugModel;
import com.lkx.jvm.sandbox.core.util.CheckUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取特定类的静态属性
 * <p>
 * 这里的实现逻辑:
 * 1. 从一个静态入口出发（可能是字段、可能是方法），得到一个对象
 * 2. 这个对象又可以继续往下递归执行一直得到你想要的结果
 * <p>
 * ; 代表命令分隔符
 * # 代表需要执行的是方法
 * . 代表需要执行的是属性，默认没有#就是属性。
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/22 - 14:07
 */
public class GetStaticCommandProcess implements CommandDebugProcess<Object> {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public CommandEnums.Debug command() {
        return CommandEnums.Debug.getStatic;
    }

    @Override
    public Object invoke(Instrumentation inst, CommandDebugModel req) {
        CheckUtils.isRequireNotNull(req.getMethodPattern(), "methodPattern");
        CheckUtils.isRequireNotNull(req.getClassNamePattern(), "classNamePattern");

        try {
            Class<?> clazz = Class.forName(req.getClassNamePattern());
            return DebugMethodUtils.getInvokeResult(req.getMethodPattern(), clazz);
        } catch (Exception e) {
            logger.error("getstatic error", e);
            return e.getMessage();
        }
    }


}
