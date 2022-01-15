package com.alibaba.jvm.sandbox.module.manager.model;

/**
 * 泛型信息
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/13 - 10:53
 */
public class GenericTypeInfo {

    private Class<?> clazz;

    private boolean isCollects;

    private boolean isMap;

    public GenericTypeInfo(Class<?> clazz, boolean isCollects, boolean isMap) {
        this.clazz = clazz;
        this.isCollects = isCollects;
        this.isMap = isMap;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public boolean isCollects() {
        return isCollects;
    }

    public void setCollects(boolean collects) {
        isCollects = collects;
    }

    public boolean isMap() {
        return isMap;
    }

    public void setMap(boolean map) {
        isMap = map;
    }
}
