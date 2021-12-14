package com.lkx.jvm.sandbox.core.factory;

import com.lkx.jvm.sandbox.core.compoents.InjectResource;

/**
 * 注入对象资源
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/13 - 18:42
 */
public interface InjectObjectResource {
    /**
     * 注册class信息
     *
     * @param obj
     */
    public void registerObjectMetaInfo(Object obj);

    /**
     * 注册集合型对象
     *
     * @param interfaces
     * @param object
     * @param <T>
     */
    public <T> void registerList(Class<?> interfaces, T object);

    /**
     * 注册对象
     *
     * @param clazz
     * @param object
     * @param <T>
     */
    public <T> void registerObject(Class<?> clazz, Object object);

    /**
     * 注册对象
     *
     * @param object
     * @param <T>
     */
    public <T> void registerObject(Object object);

    /**
     * 设置注入资源处理器
     *
     * @param injectResource
     */
    public void setInjectResource(InjectResource injectResource);
}
