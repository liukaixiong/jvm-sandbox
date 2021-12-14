package com.lkx.jvm.sandbox.core.factory;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 分组容器存储类
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/15 - 18:44
 */
public class GlobalInstanceFactory implements ObjectFactoryService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private ObjectFactoryService instanceFactory;

    private PluginInstanceFactory pluginInstanceFactory;

    public GlobalInstanceFactory(ObjectFactoryService instanceFactory, PluginInstanceFactory pluginInstanceFactory) {
        this.instanceFactory = instanceFactory;
        this.pluginInstanceFactory = pluginInstanceFactory;
    }

    @Override
    public <T> T getObject(Class<T> clazz) {
        T object = instanceFactory.getObject(clazz);

        if (object == null) {
            List<T> pluginInstanceList = pluginInstanceFactory.getPluginInstanceList(clazz);
            if (pluginInstanceList.size() == 0) {
                return null;
            } else if (pluginInstanceList.size() > 1) {
                logger.warn(" 获取对象是多个! " + clazz.getName());
            }
            object = pluginInstanceList.get(0);
        }

        return object;
    }

    @Override
    public <T> List<T> getList(Class<T> interfaces) {
        List<T> resultList = new ArrayList<>();

        List<T> list = instanceFactory.getList(interfaces);
        if (CollectionUtils.isNotEmpty(list)) {
            resultList.addAll(list);
        }

        List<T> pluginInstanceList = pluginInstanceFactory.getPluginInstanceList(interfaces);

        if (CollectionUtils.isNotEmpty(pluginInstanceList)) {
            resultList.addAll(pluginInstanceList);
        }

        return resultList;
    }
}
