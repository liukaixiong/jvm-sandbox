package com.lkx.jvm.sandbox.core.factory;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 插件对象工厂
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/9 - 16:51
 */
public class PluginObjectFactory extends InstanceFactory {

    private Logger log = LoggerFactory.getLogger(getClass());

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

}
