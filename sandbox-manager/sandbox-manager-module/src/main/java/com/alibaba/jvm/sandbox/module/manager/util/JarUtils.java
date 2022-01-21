package com.alibaba.jvm.sandbox.module.manager.util;

import com.lkx.jvm.sandbox.core.classloader.ManagerClassLoader;
import com.lkx.jvm.sandbox.core.compoents.InjectResource;
import org.apache.commons.lang3.reflect.FieldUtils;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/7 - 15:53
 */
public class JarUtils {
    /**
     * 加载一个jar包中的class对象，并转换成实例集合
     *
     * @param jarPath
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> loadObjectList(String jarPath, Class<T> clazz) throws Exception {
        return loadObjectList(jarPath, clazz, null);
    }

    public static <T> List<T> loadObjectList(String jarPath, Class<T> clazz, InjectResource injectResource) throws Exception {
        File jarFile = new File(jarPath);
        List<T> objList = new ArrayList<>();

        ManagerClassLoader managerClassLoader = new ManagerClassLoader(jarFile.getName(), new URL[]{new URL("file:" + jarFile.getPath())});
//        ModuleJarClassLoader moduleJarClassLoader = new ModuleJarClassLoader(jarFile);
        final ServiceLoader<T> moduleServiceLoader = ServiceLoader.load(clazz, managerClassLoader);
        final Iterator<T> moduleIt = moduleServiceLoader.iterator();
        while (moduleIt.hasNext()) {

            final T module;
            try {
                module = moduleIt.next();
            } catch (Throwable cause) {
                continue;
            }

            final Class<?> classOfModule = module.getClass();

            // 如果有注入对象
            if (injectResource != null) {
                for (final Field resourceField : FieldUtils.getFieldsWithAnnotation(classOfModule, Resource.class)) {
                    final Class<?> fieldType = resourceField.getType();
                    Object fieldObject = injectResource.getFieldValue(fieldType);
                    if (fieldObject != null) {
                        FieldUtils.writeField(
                                resourceField,
                                module,
                                fieldObject,
                                true
                        );
                    }
                }
            }
            System.out.println(module);
            objList.add(module);
        }
        return objList;
    }


//    public static void main(String[] args) throws Exception {
//        String jarFile = "E:\\study\\sandbox\\sandbox-module\\cat-plugin-1.3.3-jar-with-dependencies.jar";
//        loadObjectList(jarFile, Module.class);
//    }
}
