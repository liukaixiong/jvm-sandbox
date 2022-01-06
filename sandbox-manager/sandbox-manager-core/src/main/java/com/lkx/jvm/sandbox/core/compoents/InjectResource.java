package com.lkx.jvm.sandbox.core.compoents;

/**
 * 注入资源对象
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/7 - 16:23
 */
public interface InjectResource {

    /**
     * 获取注入对象
     *
     * @param resourceField
     * @return
     */
    Object getFieldValue(Class<?> resourceField);

    /**
     * 实例对象被返回的处理
     *
     * @param obj
     */
    void afterProcess(Object obj);

}
