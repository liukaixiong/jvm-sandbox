package com.lkx.jvm.sandbox.core.factory;

import java.util.List;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/13 - 16:56
 */
public interface ObjectFactoryService {

    /**
     * 获取对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getObject(Class<T> clazz);

    /**
     * 获取特定的对象集合
     *
     * @param interfaces
     * @param <T>
     * @return
     */
    <T> List<T> getList(Class<T> interfaces);
}
