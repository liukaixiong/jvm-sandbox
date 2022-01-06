package com.alibaba.jvm.sandbox.module.manager.factory;

import com.lkx.jvm.sandbox.core.enums.FactoryTypeEnums;
import com.lkx.jvm.sandbox.core.factory.TypeFactoryService;
import com.lkx.jvm.sandbox.core.util.BooleanOptional;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理器应用上下文
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/31 - 14:44
 */
@Component
public class ManagerApplicationContext implements ApplicationContextAware, TypeFactoryService {

    private ApplicationContext applicationContext;

    @Override
    public FactoryTypeEnums type() {
        return FactoryTypeEnums.CONTEXT;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T getObject(Class<T> clazz) {
        List<T> values = getObjectList(clazz);
        return BooleanOptional.ofList(values, values.size() == 1, k -> k.get(0));
    }

    @Override
    public <T> List<T> getList(Class<T> interfaces) {
        return getObjectList(interfaces);
    }

    private <T> List<T> getObjectList(Class<T> clazz) {
        return new ArrayList<>(applicationContext.getBeansOfType(clazz).values());
    }
}
