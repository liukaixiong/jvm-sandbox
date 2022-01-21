package com.sandbox.manager.api;

/**
 * 支持异常的消费者
 *
 * @param <T>
 */
public interface ThrowableConsumerSupport<T> {
    /**
     * 支持异常外抛的实现
     *
     * @param run
     * @throws Throwable
     */
    void accept(T run) throws Throwable;
}