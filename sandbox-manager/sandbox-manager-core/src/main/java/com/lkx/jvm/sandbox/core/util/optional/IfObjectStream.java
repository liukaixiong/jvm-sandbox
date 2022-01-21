package com.lkx.jvm.sandbox.core.util.optional;

/**
 * 对象条件操作
 * <p>
 * 当持有对象为null时，该对象的操作均为失效
 * <p>
 * 当对象不为null时，以下方法才会正式启用。
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/13 - 17:03
 */
public class IfObjectStream<T> extends AbstractOptionalService<T> {

    public IfObjectStream(T obj) {
        super(obj);
    }

}
