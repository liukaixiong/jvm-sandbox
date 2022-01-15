package com.alibaba.jvm.sandbox.module.manager.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/14 - 16:59
 */
public class TypePoolTools {

    private static final Map<Class<?>, Function<String, Object>> primitiveSet = new HashMap<>();

    static {
        primitiveSet.put(Integer.class, Integer::parseInt);
        primitiveSet.put(Long.class, Long::parseLong);
        primitiveSet.put(Boolean.class, Boolean::parseBoolean);
        primitiveSet.put(Date.class, TypePoolTools::parseDate);
        primitiveSet.put(String.class, s -> s);
        primitiveSet.put(Double.class, Double::parseDouble);
        primitiveSet.put(Float.class, Float::parseFloat);
    }

    /**
     * 是否是基础类型
     *
     * @param clazz
     * @return
     */
    public static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || primitiveSet.containsKey(clazz);
    }

    private static Date parseDate(String date) {
        try {
            if (date.length() == 10) {
                return DateFormatUtils.ISO_DATE_FORMAT.parse(date);
            } else {
                return DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT.parse(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 转换对象
     *
     * @param value
     * @param clazz
     * @return
     */
    public static Object convertValue(String value, Class<?> clazz) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        if (isPrimitive(clazz)) {
            try {
                return primitiveSet.get(clazz).apply(value);
            } catch (Exception e) {
                e.printStackTrace();
                // 如果遇见转换故障
                return null;
            }
        }
        return value;
    }

}
