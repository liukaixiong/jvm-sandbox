package com.lkx.jvm.sandbox.core.factory;

/**
 * 全局工厂静态帮助类
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/13 - 19:13
 */
public class GlobalFactoryHelper {

    private static GlobalInstanceFactory INSTANCE;

    public static void setInstance(GlobalInstanceFactory instance) {
        GlobalFactoryHelper.INSTANCE = instance;
    }

    public static GlobalInstanceFactory getInstance() {
        return INSTANCE;
    }

}
