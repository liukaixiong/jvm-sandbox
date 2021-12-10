package com.alibaba.jvm.sandbox.module.manager.components;

import com.lkx.jvm.sandbox.core.compoents.GroupContainerHelper;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;

import java.lang.reflect.Constructor;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/7 - 14:38
 */
public class CommandContainerHelper extends GroupContainerHelper {

    private static CommandContainerHelper INSTANCE = new CommandContainerHelper();

    public static CommandContainerHelper getInstance() {
        return INSTANCE;
    }

    public AbstractCommandInvoke getCommandInvoke(String commandEnums, CommandWatcherInfoModel commandInfoModel) throws Exception {
        Class clazz = (Class) getGroupMap().get(AbstractCommandInvoke.class.getSimpleName() + "-" + commandEnums);
        Constructor constructor = clazz.getDeclaredConstructor(CommandWatcherInfoModel.class);
        return (AbstractCommandInvoke) constructor.newInstance(new Object[]{commandInfoModel});
    }

    public void registerWatcherCommandInvoke(CommandEnums.Watcher commandEnums, Class clazz) {
        getGroupMap().put(AbstractCommandInvoke.class.getSimpleName() + "-" + commandEnums.name(), clazz);
    }
}
