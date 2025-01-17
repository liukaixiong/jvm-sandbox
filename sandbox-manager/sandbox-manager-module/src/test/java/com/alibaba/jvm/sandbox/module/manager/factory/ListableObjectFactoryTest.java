package com.alibaba.jvm.sandbox.module.manager.factory;

import com.alibaba.jvm.sandbox.module.manager.plugin.PluginManager;
import org.junit.Test;
import org.springframework.beans.factory.wiring.BeanConfigurerSupport;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ListableObjectFactoryTest {

    @Test
    public void testGetObject() {
//        CombinationObjectFactory listableObjectFactory = new CombinationObjectFactory();
//        listableObjectFactory.addObjectFactoryServices(new TestStringFactory());
//        listableObjectFactory.addObjectFactoryServices(new TestIntegerFactory());
//
//
//        List<String> list = listableObjectFactory.getList(String.class);
//        System.out.println(list);

    }

    @Test
    public void testGetList() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(BeanConfigurerSupport.class);
        context.refresh();


        PluginManager pluginManager = new PluginManager();

        BeanConfigurerSupport beanConfigurerSupport = context.getBean(BeanConfigurerSupport.class);
        beanConfigurerSupport.configureBean(pluginManager);

        System.out.println(pluginManager);

    }
}