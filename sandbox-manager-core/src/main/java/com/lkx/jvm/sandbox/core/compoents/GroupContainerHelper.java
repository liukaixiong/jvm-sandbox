package com.lkx.jvm.sandbox.core.compoents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分组容器存储类
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/15 - 18:44
 */
public class GroupContainerHelper {

    private static GroupContainerHelper INSTANCE = new GroupContainerHelper();

    private Map<String, Object> groupMap = new ConcurrentHashMap<>();

    public static GroupContainerHelper getInstance() {
        return INSTANCE;
    }

    /**
     * 注册一组接口的实现集合
     *
     * @param interfaces
     * @param object
     * @param <T>
     */
    public <T> void registerList(Class<?> interfaces, T object) {
        List<T> list = (List<T>) groupMap.computeIfAbsent(interfaces.getSimpleName(), k -> new ArrayList<T>());
        list.add(object);
    }

    public <T> T getObject(String key, Class<T> clazz) {
        return (T) groupMap.get(clazz.getSimpleName() + "-" + key);
    }

    public <T> T getObject(Class<T> clazz) {
        return (T) groupMap.get(clazz.getSimpleName());
    }

    public <T> void registerObject(String key, T object, Class<T> clazz) {
        groupMap.put(clazz.getSimpleName() + "-" + key, object);
    }

    public <T> void registerObject(Class<?> clazz, Object object) {
        groupMap.put(clazz.getSimpleName(), object);
    }

    public <T> void registerObject(Object object) {
        groupMap.put(object.getClass().getSimpleName(), object);
    }

    /**
     * 获取一组接口的实现集合
     *
     * @param interfaces
     * @return
     */
    public <T> List<T> getList(Class<T> interfaces) {
        return (List<T>) groupMap.get(interfaces.getSimpleName());
    }

    protected Map<String, Object> getGroupMap() {
        return groupMap;
    }
}
