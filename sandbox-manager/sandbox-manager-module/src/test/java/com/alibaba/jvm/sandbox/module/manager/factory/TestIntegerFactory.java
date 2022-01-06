package com.alibaba.jvm.sandbox.module.manager.factory;

import com.lkx.jvm.sandbox.core.factory.ObjectFactoryService;

import java.util.Arrays;
import java.util.List;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/31 - 16:29
 */
public class TestIntegerFactory implements ObjectFactoryService {

    private final List<Integer> list = Arrays.asList(1, 2, 3);

    private final Integer str = 456;

    @Override
    public <T> T getObject(Class<T> clazz) {
        if (clazz == Integer.class) {
            return (T) str;
        }
        return null;
    }

    @Override
    public <T> List<T> getList(Class<T> interfaces) {
        if (interfaces == Integer.class) {
            return (List<T>) list;
        }
        return null;
    }
}
