package com.alibaba.jvm.sandbox.module.manager.components;

import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 命令容器缓存
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/7 - 14:38
 */
public class CommandContainerHelper {

    private static CommandContainerHelper INSTANCE = new CommandContainerHelper();

    private Map<String, Object> groupMap = new ConcurrentHashMap<>();

    public static CommandContainerHelper getInstance() {
        return INSTANCE;
    }

    public AbstractCommandInvoke getCommandInvoke(String commandEnums, CommandWatcherInfoModel commandInfoModel) throws Exception {
        Class clazz = (Class) groupMap.get(AbstractCommandInvoke.class.getSimpleName() + "-" + commandEnums);
        Constructor constructor = clazz.getDeclaredConstructor(CommandWatcherInfoModel.class);
        return (AbstractCommandInvoke) constructor.newInstance(new Object[]{commandInfoModel});
    }

    public void registerWatcherCommandInvoke(CommandEnums.Watcher commandEnums, Class clazz) {
        groupMap.put(AbstractCommandInvoke.class.getSimpleName() + "-" + commandEnums.name(), clazz);
    }

}
