package com.lkx.jvm.sandbox.core.util;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/20 - 15:41
 */
public class AdviceUtils {

    public static void isSlowTime(MethodSupplier supplier, Integer time, Consumer<Long> consumer) {
        Stopwatch started = Stopwatch.createStarted();
        supplier.run();
        long elapsed = started.elapsed(TimeUnit.MILLISECONDS);
        if (elapsed > time) {
            consumer.accept(elapsed);
        }
    }

    /**
     * 获取方法全称
     *
     * @param advice
     * @return
     */
    public static String getMethodFullName(Advice advice) {
        return advice.getBehavior().getDeclaringClass().getName() + "#" + advice.getBehavior().getName();
    }

    /**
     * 利用反射來填充用戶的
     * @param advice
     * @param index
     * @param methodName
     * @param clazz
     * @param args
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T getObjectValue(Advice advice,int index,String methodName,Class<T> clazz,Object... args) throws Exception {
        Object value = MethodUtils.invokeMethod(advice.getParameterArray()[index],methodName,args);
        if(value == null){
            return null;
        }
        return (T) value;
    }

    public interface MethodSupplier {
        /**
         * 运行方法
         */
        void run();
    }
}
