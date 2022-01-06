package com.lkx.jvm.sandbox.core.factory;

import com.lkx.jvm.sandbox.core.enums.FactoryTypeEnums;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/4 - 14:33
 */
public interface TypeFactoryService extends ObjectFactoryService {
    /**
     * 工厂类型
     *
     * @return
     */
    FactoryTypeEnums type();
}
