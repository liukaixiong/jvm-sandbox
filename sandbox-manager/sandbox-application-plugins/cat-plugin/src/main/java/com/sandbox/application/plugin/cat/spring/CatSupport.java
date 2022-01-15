package com.sandbox.application.plugin.cat.spring;

import com.sandbox.manager.api.PluginPropertiesRegisterSupport;
import org.springframework.stereotype.Component;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/12 - 11:30
 */
@Component
public class CatSupport implements PluginPropertiesRegisterSupport {
    @Override
    public String key() {
        return "cat";
    }
}
