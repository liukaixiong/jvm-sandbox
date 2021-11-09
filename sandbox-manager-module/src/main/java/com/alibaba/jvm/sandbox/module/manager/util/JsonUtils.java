package com.alibaba.jvm.sandbox.module.manager.util;

import com.alibaba.fastjson.JSON;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/8 - 14:54
 */
public class JsonUtils {

    public static String toJsonString(Object obj) {
        if (obj == null) {
            return "";
        } else if (obj instanceof String) {
            return obj.toString();
        }
        return JSON.toJSONString(obj);
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }


}
