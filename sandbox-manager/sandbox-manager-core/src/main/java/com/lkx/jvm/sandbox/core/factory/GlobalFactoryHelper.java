package com.lkx.jvm.sandbox.core.factory;

import com.lkx.jvm.sandbox.core.enums.FactoryTypeEnums;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 全局工厂静态帮助类
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/13 - 19:13
 */
public class GlobalFactoryHelper {

    private static GlobalInstanceFactory INSTANCE = new GlobalInstanceFactory();

    protected static void registerFactory(GlobalInstanceFactory globalInstanceFactory) {
        GlobalFactoryHelper.INSTANCE = globalInstanceFactory;
    }

    public static ObjectFactoryService getInstance(FactoryTypeEnums factoryTypeEnums) {
        return INSTANCE.getFactory(factoryTypeEnums);
    }

    public static AbstractCacheFactory core() {
        return (AbstractCacheFactory) getInstance(FactoryTypeEnums.CORE);
    }

    public static ObjectFactoryService plugin() {
        // 如果没有插进的前提下，按照应用获取
        return ObjectUtils.defaultIfNull(getInstance(FactoryTypeEnums.PLUGIN), getInstance());
    }

    public static ObjectFactoryService getInstance() {
        return getInstance(FactoryTypeEnums.CONTEXT);
    }

}
