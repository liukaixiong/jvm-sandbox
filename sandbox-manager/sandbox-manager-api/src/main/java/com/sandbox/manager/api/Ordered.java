package com.sandbox.manager.api;

/**
 * 接口排序
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/8 - 15:29
 */
public interface Ordered {

    int HIGHEST_PRECEDENCE = -2147483648;

    int LOWEST_PRECEDENCE = 2147483647;

    int getOrder();
}
