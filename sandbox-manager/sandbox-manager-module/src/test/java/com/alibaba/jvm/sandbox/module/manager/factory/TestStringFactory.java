package com.alibaba.jvm.sandbox.module.manager.factory;

import com.lkx.jvm.sandbox.core.factory.ObjectFactoryService;

import java.util.Arrays;
import java.util.List;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/31 - 16:29
 */
public class TestStringFactory implements ObjectFactoryService {

    private final List<String> list = Arrays.asList("A", "B", "C");

    private final String str = "lkx";

    @Override
    public <T> T getObject(Class<T> clazz) {
        if (clazz == String.class) {
            return (T) str;
        }
        return null;
    }

    @Override
    public <T> List<T> getList(Class<T> interfaces) {
        if (interfaces == String.class) {
            return (List<T>) list;
        }
        return null;
    }
}
