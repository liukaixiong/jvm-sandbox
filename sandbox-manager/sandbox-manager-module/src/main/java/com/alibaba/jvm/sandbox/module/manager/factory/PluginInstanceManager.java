package com.alibaba.jvm.sandbox.module.manager.factory;

import com.alibaba.jvm.sandbox.module.manager.spring.PluginManager;
import com.lkx.jvm.sandbox.core.enums.FactoryTypeEnums;
import com.lkx.jvm.sandbox.core.factory.TypeFactoryService;
import com.lkx.jvm.sandbox.core.util.BooleanOptional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 插件管理工厂
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/31 - 14:44
 */
@Component
public class PluginInstanceManager implements TypeFactoryService {

    @Autowired
    private PluginManager pluginManager;

    @Override
    public FactoryTypeEnums type() {
        return FactoryTypeEnums.PLUGIN;
    }

    @Override
    public <T> T getObject(Class<T> clazz) {
        List<T> objectList = pluginManager.getObjectList(clazz);
        return BooleanOptional.ofList(objectList, objectList.size() == 1, k -> k.get(0));
    }

    @Override
    public <T> List<T> getList(Class<T> interfaces) {
        // 插件是可以往上找的.
        return pluginManager.getObjectList(interfaces);
    }
}
