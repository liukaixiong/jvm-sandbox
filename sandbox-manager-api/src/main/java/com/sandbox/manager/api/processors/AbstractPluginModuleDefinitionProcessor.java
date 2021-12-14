package com.sandbox.manager.api.processors;

import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.PluginModule;
import com.sandbox.manager.api.model.enhance.EnhanceClassInfo;
import org.kohsuke.MetaInfServices;

import java.util.List;
import java.util.Set;

/**
 * 插件模块定义执行器，这里主要负责定义需要增强的信息以及增强后的回调处理器。
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/8 - 14:23
 */
@MetaInfServices
public abstract class AbstractPluginModuleDefinitionProcessor implements PluginModule {

    /**
     * 插件名称
     *
     * @return
     */
    public abstract String pluginName();

    /**
     * 获取子类传递需要增强的类
     *
     * @return
     */
    public abstract List<EnhanceClassInfo> getEnhanceClassInfos();

    /**
     * 获取对应的执行器
     *
     * @return
     */
    public abstract Set<AdviceNameDefinition> getMethodAdviceInvoke();

}
