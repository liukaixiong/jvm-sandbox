package com.alibaba.jvm.sandbox.module.manager.spring;

import com.lkx.jvm.sandbox.core.factory.GlobalFactoryHelper;
import com.sandbox.manager.api.PluginPropertiesRegisterSupport;
import com.sandbox.manager.api.SpringLoadCompleted;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 刷新完成之后回调属性注册
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/12 - 10:38
 */
@Component
public class PluginPropertiesRegisterCallback implements SpringLoadCompleted {



    @Override
    public void refreshCallback() {
        List<PluginPropertiesRegisterSupport> pluginPropertiesRegisterSupports = GlobalFactoryHelper.plugin().getList(PluginPropertiesRegisterSupport.class);

        if (pluginPropertiesRegisterSupports == null || pluginPropertiesRegisterSupports.size() == 0) {
            return;
        }

        pluginPropertiesRegisterSupports.stream().map(PluginPropertiesRegisterSupport::key).forEach(key -> {

        });
    }
}
