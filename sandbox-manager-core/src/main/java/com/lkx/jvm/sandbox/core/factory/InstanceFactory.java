package com.lkx.jvm.sandbox.core.factory;

import com.lkx.jvm.sandbox.core.compoents.InjectResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局实例工厂
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/13 - 16:41
 */
public class InstanceFactory implements ObjectFactoryService, InjectObjectResource {
    protected Logger log = LoggerFactory.getLogger(getClass());

    protected InjectResource injectResource;

    @Override
    public void setInjectResource(InjectResource injectResource) {
        this.injectResource = injectResource;
    }

    private Map<String, Object> groupMap = new ConcurrentHashMap<>();

    /**
     * 注册一组接口的实现集合
     *
     * @param clazz
     * @param object
     * @param <T>
     */
    @Override
    public <T> void registerList(Class<?> clazz, T object) {
        List<T> list = (List<T>) groupMap.computeIfAbsent(getObjectKey(clazz), k -> new ArrayList<T>());
        list.add(object);
    }


    @Override
    public <T> T getObject(Class<T> clazz) {
        return (T) groupMap.get(getObjectKey(clazz));
    }

    public <T> void registerObject(String key, T object, Class<T> clazz) {
        groupMap.put(getObjectKey(clazz) + "-" + key, object);
    }

    @Override
    public <T> void registerObject(Class<?> clazz, Object object) {
        groupMap.put(getObjectKey(clazz), object);
    }

    @Override
    public <T> void registerObject(Object object) {
        String objectKey = getObjectKey(object.getClass());

        Object oldObject = groupMap.put(objectKey, object);

        if (oldObject != null) {
            log.warn("plugin object cache There is repeated" + objectKey);
        }
    }

    /**
     * 获取一组接口的实现集合
     *
     * @param interfaces
     * @return
     */
    @Override
    public <T> List<T> getList(Class<T> interfaces) {
        return (List<T>) groupMap.get(getObjectKey(interfaces));
    }

    protected String getObjectKey(Class<?> clazz) {
        return clazz.getName();
    }

    /**
     * 注册对象的所有信息
     * metainfo
     *
     * @param obj
     */
    @Override
    public void registerObjectMetaInfo(Object obj) {
        Class<?> clazz = obj.getClass();
        Set<Class<?>> duplicatesSet = new HashSet<>();
        // 先注册自己
        registerObject(obj);
        // 加入已加载
        duplicatesSet.add(clazz);
        // 在注册接口
        loadInterfaceInfo(duplicatesSet, clazz, obj);
        // 注册父类
        loadSuperClassInfo(duplicatesSet, clazz.getSuperclass(), obj);
    }

    /**
     * 构建类的关系对象
     *
     * @param duplicatesSet
     * @param clazz
     * @param obj
     */
    private void loadSuperClassInfo(Set<Class<?>> duplicatesSet, Class<?> clazz, Object obj) {
        if (clazz == Object.class || duplicatesSet.contains(clazz)) {
            return;
        }
        // 加入防重复
        duplicatesSet.add(clazz);
        // 注册成List结构
        registerList(clazz, obj);
        // 查找当前类的所有接口
        loadInterfaceInfo(duplicatesSet, clazz, obj);
        // 继续查找上级父类
        loadSuperClassInfo(duplicatesSet, clazz.getSuperclass(), obj);
    }


    /**
     * 加载接口信息
     *
     * @param duplicatesSet
     * @param clazz
     * @param obj
     */
    private void loadInterfaceInfo(Set<Class<?>> duplicatesSet, Class<?> clazz, Object obj) {
        Class<?>[] interfaces = clazz.getInterfaces();
        // 将接口类进行分组
        if (interfaces.length > 0) {
            for (int i = 0; i < interfaces.length; i++) {
                Class<?> anInterface = interfaces[i];
                //排除重复的
                if (duplicatesSet.contains(anInterface)) {
                    continue;
                }
                this.registerList(anInterface, obj);
                duplicatesSet.add(anInterface);
                loadInterfaceInfo(duplicatesSet, anInterface, obj);
            }
        }
    }
}
