package com.lkx.jvm.sandbox.core.factory;

import java.util.List;

/**
 * 缓存工厂
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/4 - 14:47
 */
public abstract class AbstractCacheFactory implements TypeFactoryService {

    /**
     * 注册对象
     *
     * @param key    缓存标识
     * @param object 缓存对象
     */
    public abstract void registerObject(Class key, Object object);


    /**
     * 缓存集合，缓存标识
     *
     * @param key
     * @param object
     */
    public abstract void registerList(Class key, List<?> object);

}
