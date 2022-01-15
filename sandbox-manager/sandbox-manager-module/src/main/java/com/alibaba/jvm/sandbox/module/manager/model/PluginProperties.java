package com.alibaba.jvm.sandbox.module.manager.model;


import java.util.ArrayList;
import java.util.List;

/**
 * 增强配置集合
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/12 - 15:31
 */
public class PluginProperties {

    private List<EnhanceConfig> enhanceConfigs = new ArrayList<>();

    public List<EnhanceConfig> getEnhanceConfigs() {
        return enhanceConfigs;
    }

    public void setEnhanceConfigs(List<EnhanceConfig> enhanceConfigs) {
        this.enhanceConfigs = enhanceConfigs;
    }
}
