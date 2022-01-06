package com.lkx.jvm.sandbox.core.factory;

import com.lkx.jvm.sandbox.core.enums.FactoryTypeEnums;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分组容器存储类
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/15 - 18:44
 */
public class GlobalInstanceFactory {

    private Map<FactoryTypeEnums, TypeFactoryService> cacheMap = new ConcurrentHashMap<>();

    /**
     * 注册全局工厂，将所有工厂全部注册进来，按照类型划分
     *
     * @param objectFactoryServices
     */
    public void registerFactory(TypeFactoryService... objectFactoryServices) {
        Objects.requireNonNull(objectFactoryServices);
        Arrays.stream(objectFactoryServices).forEach((factory) -> cacheMap.put(factory.type(), factory));
        GlobalFactoryHelper.registerFactory(this);
    }

    public ObjectFactoryService getFactory(FactoryTypeEnums factoryTypeEnums) {
        return cacheMap.get(factoryTypeEnums);
    }
}
