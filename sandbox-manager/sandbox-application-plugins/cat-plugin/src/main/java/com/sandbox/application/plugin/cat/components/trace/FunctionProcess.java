package com.sandbox.application.plugin.cat.components.trace;

/**
 * @Module TODO
 * @Description TODO
 * @Author liukaixiong
 * @Date 2020/12/28 18:36
 */
public interface FunctionProcess<T, R> {
    /**
     * @param obj
     * @return
     */
    R process(T obj) throws Exception;
}
