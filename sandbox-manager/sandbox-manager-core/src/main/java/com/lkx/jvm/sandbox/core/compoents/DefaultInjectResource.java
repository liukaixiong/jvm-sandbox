package com.lkx.jvm.sandbox.core.compoents;

import com.lkx.jvm.sandbox.core.factory.GlobalFactoryHelper;

/**
 * 默认注入工厂
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/8 - 13:40
 */
public class DefaultInjectResource implements InjectResource {

    @Override
    public Object getFieldValue(Class<?> resourceField) {
        return GlobalFactoryHelper.getInstance().getObject(resourceField);
    }

    @Override
    public void afterProcess(Object obj) {
//        Class<?> clazz = obj.getClass();
//
//        builderObjectCache(clazz, obj);
//
//        instanceFactory.registerObject(obj);
    }


//    public void builderObjectCache(Class<?> clazz, Object obj) {
//
//        if (clazz == Object.class) {
//            return;
//        }
//
//        instanceFactory.registerList(clazz, obj);
//
//        Class<?>[] interfaces = clazz.getInterfaces();
//        // 将接口类进行分组
//        if (interfaces.length > 0) {
//            for (int i = 0; i < interfaces.length; i++) {
//                Class<?> anInterface = interfaces[i];
//                instanceFactory.registerList(anInterface, obj);
//            }
//        }
//
//        builderObjectCache(clazz.getSuperclass(), obj);
//    }

}
