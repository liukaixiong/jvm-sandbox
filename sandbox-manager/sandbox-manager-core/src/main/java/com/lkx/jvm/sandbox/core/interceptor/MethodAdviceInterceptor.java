package com.lkx.jvm.sandbox.core.interceptor;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.sandbox.manager.api.MethodAdviceInvoke;

import java.util.Map;

/**
 * 方法拦截
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/20 - 16:01
 */
public interface MethodAdviceInterceptor {

    default void before(Advice advice, MethodAdviceInvoke consumer, Map<String, Object> context) {

    }

    default void after(Advice advice, MethodAdviceInvoke consumer, Map<String, Object> context) {

    }

    default void error(Advice advice, MethodAdviceInvoke consumer, Map<String, Object> context, Throwable throwable) {

    }
}
