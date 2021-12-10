package com.lkx.jvm.sandbox.core.factory;

import com.lkx.jvm.sandbox.core.compoents.InjectResource;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 插件对象工厂
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/9 - 16:51
 */
public class PluginObjectFactory {

    private Logger log = LoggerFactory.getLogger(getClass());

    private Map<String, Object> pluginObjectCache = new HashMap<>();

    private InjectResource injectResource;

    /**
     * 加载对应的实例对象
     *
     * @param clazz
     * @return
     */
    public void loadObjectList(Class<?> clazz, ClassLoader classLoader) {

        ServiceLoader<?> componentsServiceLoader = ServiceLoader.load(clazz, classLoader);

        final Iterator<?> moduleIt = componentsServiceLoader.iterator();

        while (moduleIt.hasNext()) {

            final Object module;
            try {
                module = moduleIt.next();
            } catch (Throwable cause) {
                log.error("error load jar", cause);
                continue;
            }

            // 如果有注入对象
            injectResource(module);

            // 注入缓存
            registerObjectMetaInfo(module);
        }
    }

    /**
     * 注入资源
     *
     * @param module
     * @param <T>
     */
    private <T> void injectResource(T module) {
        if (injectResource != null) {
            for (final Field resourceField : FieldUtils.getFieldsWithAnnotation(module.getClass(), Resource.class)) {
                final Class<?> fieldType = resourceField.getType();
                Object fieldObject = injectResource.getFieldValue(fieldType);
                if (fieldObject != null) {
                    try {
                        FieldUtils.writeField(
                                resourceField,
                                module,
                                fieldObject,
                                true
                        );
                    } catch (Exception e) {
                        log.warn(" set Value error : " + e.getMessage());
                    }
                }
            }
            injectResource.afterProcess(module);
        }
    }

    public <T> T getObject(String key, Class<T> clazz) {
        return (T) pluginObjectCache.get(getObjectKey(clazz) + "-" + key);
    }

    public <T> T getObject(Class<T> clazz) {
        return (T) pluginObjectCache.get(getObjectKey(clazz));
    }

    /**
     * 获取一组接口的实现集合
     *
     * @param interfaces
     * @return
     */
    public <T> List<T> getList(Class<T> interfaces) {
        return (List<T>) pluginObjectCache.get(getObjectKey(interfaces));
    }

    private <T> void registerList(Class<?> interfaces, T object) {
        List<T> list = (List<T>) pluginObjectCache.computeIfAbsent(getObjectKey(interfaces), k -> new ArrayList<T>());
        list.add(object);
    }

    private void registerObject(Object object) {
        String objectKey = getObjectKey(object.getClass());

        Object oldObject = pluginObjectCache.put(objectKey, object);

        if (oldObject != null) {
            log.warn("plugin object cache There is repeated" + objectKey);
        }
    }

    private String getObjectKey(Class<?> clazz) {
        return clazz.getName();
    }

    /**
     * 注册对象的所有信息
     * metainfo
     *
     * @param obj
     */
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

    public void setInjectResource(InjectResource injectResource) {
        this.injectResource = injectResource;
    }
}
