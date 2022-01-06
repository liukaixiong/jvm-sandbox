package com.alibaba.jvm.sandbox.module.manager.util;

import com.google.common.base.Splitter;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/22 - 16:24
 */
public class DebugMethodUtils {
    private static final String METHOD_INVOKE = "M";
    private static final String FIELD_INVOKE = "F";

    public static Object getInvokeResult(String methodPattern, Object obj) throws Exception {
        Object process = obj;
        List<ImmutablePair<String, String>> invokeTypeList = getInvokeTypeList(methodPattern);
        for (int i = 0; i < invokeTypeList.size(); i++) {
            ImmutablePair<String, String> immutablePair = invokeTypeList.get(i);
            String type = immutablePair.getLeft();
            String method = immutablePair.getRight();
            if (process instanceof Class) {
                if (METHOD_INVOKE.equals(type)) {
                    process = MethodUtils.invokeStaticMethod((Class<?>) process, method, true);
                } else {
                    process = FieldUtils.readDeclaredStaticField((Class<?>) process, method, true);
                }
            } else {
                if (METHOD_INVOKE.equals(type)) {
                    process = MethodUtils.invokeMethod(process, method, null);
                } else {
                    process = FieldUtils.readField(process, method, true);
                }
            }
        }
        return process;
    }

    /**
     * 获取执行组
     * <p>
     * 1. 通过;分组
     * 2. 然后判断是否存在#号,代表方法
     *
     * @param methodPattern
     * @return
     */
    private static List<ImmutablePair<String, String>> getInvokeTypeList(String methodPattern) {
        List<ImmutablePair<String, String>> resultList = new ArrayList<>();
        if (methodPattern.contains(";")) {
            List<String> groupInvoke = Splitter.on(";").trimResults().splitToList(methodPattern);
            for (int i = 0; i < groupInvoke.size(); i++) {
                String str = groupInvoke.get(i);
                resultList.add(getInvokeType(str));
            }
        } else {
            resultList.add(getInvokeType(methodPattern));
        }
        return resultList;
    }

    private static ImmutablePair<String, String> getInvokeType(String str) {
        if (str.contains("#")) {
            return ImmutablePair.of(METHOD_INVOKE, str.replace("#", ""));
        } else {
            return ImmutablePair.of(FIELD_INVOKE, str.replace("\\.", ""));
        }
    }
}
