package com.alibaba.jvm.sandbox.module.manager.components;

import com.alibaba.jvm.sandbox.module.manager.process.StackProcess;
import com.alibaba.jvm.sandbox.module.manager.process.intercept.CommandPostProcess;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandInfoModel;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ReflectionUtils;
import sun.security.provider.ConfigFile;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

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

    public AbstractCommandInvoke getCommandInvoke(String commandEnums, CommandInfoModel commandInfoModel) throws Exception {
        Class clazz = (Class) groupMap.get(AbstractCommandInvoke.class.getSimpleName() + "-" + commandEnums);
        Constructor constructor = clazz.getDeclaredConstructor(CommandInfoModel.class);
        return (AbstractCommandInvoke) constructor.newInstance(new Object[]{commandInfoModel});
    }

    public AbstractCommandInvoke getCommandInvoke(String commandEnums, CommandInfoModel commandInfoModel, CommandPostProcess commandPostProcess) throws Exception {
        Class clazz = (Class) groupMap.get(AbstractCommandInvoke.class.getSimpleName() + "-" + commandEnums);
        Constructor constructor = clazz.getDeclaredConstructor(CommandInfoModel.class, CommandPostProcess.class);
        return (AbstractCommandInvoke) constructor.newInstance(new Object[]{commandInfoModel, commandPostProcess});
    }

    public void registerCommandInvoke(CommandEnums commandEnums, Class clazz) {
        groupMap.put(AbstractCommandInvoke.class.getSimpleName() + "-" + commandEnums.name(), clazz);
    }
}
