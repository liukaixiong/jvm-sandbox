package com.alibaba.jvm.sandbox.module.manager.components;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 由于Spring的工厂类无法直接获取，所有操作都得通过反射来执行
 * <p>
 * 需要注意的是这里返回的对象，只能在最后一层级。也就是基本类型结果也可以说是值对象，如果是对象返回出去： 方法不适用。
 * <p>
 * 比如返回去的是Environment对象,那么还是得通过反射去调用他的方法,object是不实用的.
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/9 - 16:56
 */
public class SpringContextContainer {
    private Object applicationContext;

    private AtomicBoolean isLoad = new AtomicBoolean(false);

    private ApplicationContext ac;

    private static SpringContextContainer INSTANCE = new SpringContextContainer();

    public static SpringContextContainer getInstance() {
        return INSTANCE;
    }

    public void setApplicationContext(Object applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <T> Map<String, T> getBeansOfType(@Nullable Class<T> clazz) throws Exception {
        return (Map<String, T>) getApplicationObject("getBeansOfType", clazz);
    }

    public Object getBean(String beanName) throws Exception {
        return getApplicationObject("getBean", beanName);
    }

    public Object getBean(Class<? extends Annotation> annotationClass) throws Exception {
        return getApplicationObject("getBeansWithAnnotation",annotationClass);
    }

    private Object getEnvironment() throws Exception {
        return getApplicationObject("getEnvironment");
    }

    /**
     * 获取属性
     *
     * @param key
     * @return
     * @throws Exception
     */
    public String getProperties(String key) throws Exception {
        return ObjectUtils.defaultIfNull(getObject(getEnvironment(), "getProperty", key), "").toString();
    }

    /**
     * 获取工厂类中的bean，处于最底层，对应的是BeanFactory
     *
     * @param methodName 方法名称
     * @param params     参数
     * @return
     * @throws Exception
     */
    private Object getApplicationObject(String methodName, Object... params) throws Exception {
        return getObject(applicationContext, methodName, params);
    }

    /**
     * 根据对象获取特定的值
     *
     * @param object     可以是factory也可以是
     * @param methodName
     * @param params
     * @return
     * @throws Exception
     */
    private Object getObject(Object object, String methodName, Object... params) throws Exception {
        return MethodUtils.invokeMethod(object, methodName, params);
    }

    /**
     * 容器是否经过加载了
     *
     * @param load
     */
    public void setLoad(boolean load) {
        isLoad.set(load);
    }

    public boolean isLoad() {
        return isLoad.get();
    }

}
