package com.alibaba.jvm.sandbox.module.manager.interceptor.advice;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.google.common.base.Stopwatch;
import com.lkx.jvm.sandbox.core.config.GlobalOptions;
import com.lkx.jvm.sandbox.core.interceptor.MethodAdviceInterceptor;
import com.lkx.jvm.sandbox.core.util.AdviceUtils;
import com.sandbox.manager.api.MethodAdviceInvoke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 慢方法拦截
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/20 - 16:26
 */
@Component
public class SlowAdviceInterceptor implements MethodAdviceInterceptor {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String key = "slow_advice_start_time";

    @Override
    public void before(Advice advice, MethodAdviceInvoke consumer, Map<String, Object> context) {
        context.put(key, Stopwatch.createStarted());
    }

    @Override
    public void after(Advice advice, MethodAdviceInvoke consumer, Map<String, Object> context) {
        Optional.ofNullable(context.get(key)).ifPresent((watch) -> {
            Integer lowMethodMs = GlobalOptions.LOW_METHOD_MS;
            long time = ((Stopwatch) watch).elapsed(TimeUnit.MILLISECONDS);
            if (time > lowMethodMs) {
                String methodFullName = AdviceUtils.getMethodFullName(advice);
                logger.warn("增强类 : {} 拦截方法: {} 耗时: {} 超过预期: {}", consumer, methodFullName, time, lowMethodMs);
            }
        });
    }

}
