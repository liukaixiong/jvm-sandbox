package com.sandbox.demo.config.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/12 - 15:31
 */
@ConfigurationProperties(prefix = "spring.sandbox.plugin")
public class PluginProperties {

    private Map<String, List<CatProperties>> register;

    public Map<String, List<CatProperties>> getRegister() {
        return register;
    }

    public void setRegister(Map<String, List<CatProperties>> register) {
        this.register = register;
    }
}
