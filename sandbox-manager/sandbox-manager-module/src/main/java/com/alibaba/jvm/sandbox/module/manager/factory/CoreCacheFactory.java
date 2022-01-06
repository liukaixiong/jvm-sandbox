package com.alibaba.jvm.sandbox.module.manager.factory;

import com.lkx.jvm.sandbox.core.enums.FactoryTypeEnums;
import com.lkx.jvm.sandbox.core.factory.AbstractCacheFactory;
import com.lkx.jvm.sandbox.core.factory.TypeFactoryService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 核心缓存工厂，负责存放sandbox-core的一些实例
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/4 - 14:09
 */
@Component
public class CoreCacheFactory extends AbstractCacheFactory implements TypeFactoryService {

    private Map<String, Object> objectCache = new ConcurrentHashMap<>();

    @Override
    public FactoryTypeEnums type() {
        return FactoryTypeEnums.CORE;
    }

    public void registerObject(String key, Object object) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(object);

        objectCache.put(key, object);
    }

    @Override
    public void registerObject(Class key, Object object) {
        registerObject(key.getName(), object);
    }

    public void registerList(String key, List<?> object) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(object);

        List result = (List) objectCache.computeIfAbsent(key, k -> new ArrayList<>());
        result.addAll(object);
    }

    @Override
    public void registerList(Class key, List<?> object) {
        registerList(key, object);
    }

    @Override
    public <T> T getObject(Class<T> clazz) {
        return (T) objectCache.get(clazz.getName());
    }

    @Override
    public <T> List<T> getList(Class<T> interfaces) {
        return (List<T>) objectCache.get(interfaces);
    }
}
