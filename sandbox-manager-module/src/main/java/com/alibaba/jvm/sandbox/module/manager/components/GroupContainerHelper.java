package com.alibaba.jvm.sandbox.module.manager.components;

import com.alibaba.jvm.sandbox.module.manager.process.callback.CommandPostCallback;
import com.alibaba.jvm.sandbox.module.manager.process.callback.HttpCommandLogSendCallback;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandInfoModel;
import org.apache.commons.collections.iterators.ArrayListIterator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分组容器存储类
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/15 - 18:44
 */
public class GroupContainerHelper {

    private static GroupContainerHelper INSTANCE = new GroupContainerHelper();

    private Map<String, Object> groupMap = new ConcurrentHashMap<>();

    public static GroupContainerHelper getInstance() {
        return INSTANCE;
    }

    /**
     * 注册一组接口的实现集合
     *
     * @param interfaces
     * @param object
     * @param <T>
     */
    public <T> void registerList(Class<T> interfaces, T object) {
        List<T> list = (List<T>) groupMap.computeIfAbsent(interfaces.getSimpleName(), k -> new ArrayList<T>());
        list.add(object);
    }

    /**
     * 获取一组接口的实现集合
     *
     * @param interfaces
     * @param <T>
     * @return
     */
    public <T> List<T> getList(Class<T> interfaces) {
        return (List<T>) groupMap.get(interfaces.getSimpleName());
    }

    public AbstractCommandInvoke getCommandInvoke(String commandEnums, CommandInfoModel commandInfoModel) throws Exception {
        Class clazz = (Class) groupMap.get(AbstractCommandInvoke.class.getSimpleName() + "-" + commandEnums);
        Constructor constructor = clazz.getDeclaredConstructor(CommandInfoModel.class);
        return (AbstractCommandInvoke) constructor.newInstance(new Object[]{commandInfoModel});
    }

    public void registerCommandInvoke(CommandEnums commandEnums, Class clazz) {
        groupMap.put(AbstractCommandInvoke.class.getSimpleName() + "-" + commandEnums.name(), clazz);
    }

}
