package com.alibaba.jvm.sandbox.module.manager.util;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import com.alibaba.jvm.sandbox.module.manager.model.GenericTypeInfo;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Function;

/**
 * 解析属性工具
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/12 - 14:12
 */
public class ParsePropertiesUtils {
    private static Logger logger = LoggerFactory.getLogger(ParsePropertiesUtils.class);

    /**
     * 解析配置属性
     * 核心作用是用来解析SpringBoot中的属性与实体进行映射
     *
     * @param propertiesPrefix 属性前缀
     * @param key              如果是Map类型的需要指定key
     * @param function         根据生成的配置获取值
     * @param clazz            映射的实体
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T parseObject(String propertiesPrefix, String key, Function<String, String> function, Class<T> clazz) throws Exception {
        Objects.requireNonNull(propertiesPrefix, "propertiesPrefix");
        Field[] declaredFields = clazz.getDeclaredFields();
        T obj = clazz.newInstance();
        boolean isInitialize = false;
        for (Field declaredField : declaredFields) {
            Object objectValue = null;
            Class<?> type = declaredField.getType();
            String name = declaredField.getName();
            String prefixName = propertiesPrefix.concat("." + name);
            // 如果是集合
            if (TypeUtils.isAssignable(type, Collection.class)) {
                objectValue = processList(prefixName, function, declaredField);
            }
            // 如果是Map
            else if (TypeUtils.isAssignable(type, Map.class)) {
                objectValue = processMap(prefixName, key, function, declaredField);
            } else {
                objectValue = convertValue(prefixName, type, function);
            }
            // 如果是对象
            if (objectValue != null) {
                isInitialize = true;
                FieldUtils.writeField(declaredField, obj, objectValue, true);
            }
        }
        return isInitialize ? obj : null;
    }

    private static Object convertValue(String prefixName, Class<?> type, Function<String, String> function) {
        String value = function.apply(prefixName);
        return TypePoolTools.convertValue(value, type);
    }

    private static Collection processList(String prefixName, Function<String, String> function, Field declaredField) throws Exception {
        GenericTypeInfo genericType = getGenericType(declaredField);
        Class<?> clazz = genericType.getClazz();
        int index = 0;
        Collection<Object> collection = new ArrayList();
        while (true) {
            String currentKey = prefixName + "[" + index + "]";
            Object value = TypePoolTools.isPrimitive(clazz) ? convertValue(currentKey, clazz, function) : parseObject(currentKey, null, function, clazz);
            if (value == null) {
                break;
            }
            collection.add(value);
            index++;
            if (index > 100) {
                logger.warn(" 当心死循环 ... ");
            }
        }
        return collection.isEmpty() ? null : collection;
    }

    private static Map<String, Object> processMap(String prefixName, String key, Function<String, String> function, Field field) throws Exception {
        GenericTypeInfo genericType = getGenericType(field);
        String nextKey = prefixName + "." + key;
        Map<String, Object> resultMap = new HashMap<>();
        if (genericType.isCollects()) {
            resultMap.put(key, processList(nextKey, function, field));
        } else {
            resultMap.put(key, parseObject(nextKey, null, function, genericType.getClazz()));
        }
        return resultMap;
    }


    private static GenericTypeInfo getGenericType(Field field) {
        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
        return parseGenericType(field, genericType);
    }

    private static GenericTypeInfo parseGenericType(Field field, ParameterizedType genericType) {
        if (TypeUtils.isAssignable(genericType.getRawType(), Collection.class)) {
            Class<?> actualTypeArgument = (Class) genericType.getActualTypeArguments()[0];
            return new GenericTypeInfo(actualTypeArgument, true, false);
        } else if (TypeUtils.isAssignable(genericType.getRawType(), Map.class)) {
            if (genericType.getActualTypeArguments()[1].getClass() == ParameterizedTypeImpl.class) {
                ParameterizedTypeImpl actualTypeArgument = (ParameterizedTypeImpl) genericType.getActualTypeArguments()[1];
                return parseGenericType(field, actualTypeArgument);
            }
            return new GenericTypeInfo((Class<?>) genericType.getActualTypeArguments()[1], false, false);
        } else {
            return new GenericTypeInfo(field.getType(), false, false);
        }
    }

}
