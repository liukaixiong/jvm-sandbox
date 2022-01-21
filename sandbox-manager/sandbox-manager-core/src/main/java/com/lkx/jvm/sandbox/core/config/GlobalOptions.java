package com.lkx.jvm.sandbox.core.config;

/**
 * 全局配置类
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/22 - 14:28
 */
public class GlobalOptions {
    /**
     * 是否关闭子类搜索
     */
    public static volatile boolean isDisableSubClass = false;

    /**
     * 慢方法的配置
     */
    public static final Integer LOW_METHOD_MS = 10;

}
