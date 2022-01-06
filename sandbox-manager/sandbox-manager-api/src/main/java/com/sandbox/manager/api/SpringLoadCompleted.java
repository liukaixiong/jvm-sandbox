package com.sandbox.manager.api;

/**
 * Spring容器启动完毕之后的回调
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/7 - 14:25
 */
public interface SpringLoadCompleted {

    /**
     * 刷新成功的回调
     */
    default void refreshCallback() {

    }

    /**
     * 刷新失败的回调
     */
    default void errorCallback() {

    }

    /**
     * 不管成功还是失败都会回调
     */
    default void callback() {

    }

}
