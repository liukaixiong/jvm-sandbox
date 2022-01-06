//package com.alibaba.jvm.sandbox.module.manager.factory;
//
//import com.lkx.jvm.sandbox.core.factory.ObjectFactoryService;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * 多实例全局工厂
// *
// * @author liukaixiong
// * @Email liukx@elab-plus.com
// * @date 2021/12/31 - 16:11
// */
//public class CombinationObjectFactory implements ObjectFactoryService {
//
//    private final List<ObjectFactoryService> objectFactoryServices = new ArrayList<>();
//
//    public void addObjectFactoryServices(ObjectFactoryService objectFactoryServices) {
//        this.objectFactoryServices.add(objectFactoryServices);
//    }
//
//    @Override
//    public <T> T getObject(Class<T> clazz) {
//        return objectFactoryServices.stream().map(k -> k.getObject(clazz)).filter(Objects::nonNull).findFirst().get();
//    }
//
//    @Override
//    public <T> List<T> getList(Class<T> interfaces) {
//        /*
//            其实这一部分感觉设计的还是有问题，因为每个插件都继承了manager这个主实例的工厂，所以如果从主应用中加载所有插件的集合，会出现重复。
//            因为插件本身的工厂一旦找不到这个实例便会向上查找，多个插件的话，如果查找的都是主实例的话，可能就会导致重复实例
//         */
//        return objectFactoryServices.stream().map(k -> k.getList(interfaces)).filter(k -> k.size() > 0).flatMap(Collection::stream).distinct().collect(Collectors.toList());
//    }
//}
