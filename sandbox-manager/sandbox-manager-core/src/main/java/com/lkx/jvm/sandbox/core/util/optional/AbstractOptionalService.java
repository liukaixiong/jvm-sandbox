package com.lkx.jvm.sandbox.core.util.optional;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 抽象的可选操作类
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/13 - 18:39
 */
public class AbstractOptionalService<T> {

    private final T object;
    private boolean isNext = true;
    private final boolean isNull;

    public AbstractOptionalService(T object) {
        this.object = object;
        this.isNull = Objects.isNull(object);
    }

    protected boolean isNext() {
        return !isNull && this.isNext;
    }

    protected void breakNext() {
        this.isNext = false;
    }

    protected T getObject() {
        return this.object;
    }

    /**
     * 如果存在
     *
     * @param consumer 操作
     * @return
     */
    public AbstractOptionalService<T> ifPresent(Consumer<T> consumer) {
        if (!this.isNull) {
            consumer.accept(this.object);
        }
        return this;
    }

    /**
     * 等值判断
     *
     * @param conditionValue
     * @param consumer
     * @return
     */
    public AbstractOptionalService<T> eq(T conditionValue, Consumer<T> consumer) {
        return eq(isNext() && getObject().equals(conditionValue), consumer);
    }

    public void eqElse(T conditionValue, Consumer<T> consumer, Consumer<T> defaultConsumer) {
        eqElse(isNext() && getObject().equals(conditionValue), consumer, defaultConsumer);
    }

//    public AbstractOptionalService<T> eq(T conditionValue, Object defaultValue) {
//        return eq(isNext() && getObject().equals(conditionValue), () -> defaultValue);
//    }

    /**
     * 等值判断
     *
     * @param test
     * @param consumer
     * @param defaultConsumer
     */
    public void eqElse(boolean test, Consumer<T> consumer, Consumer<T> defaultConsumer) {
        eq(test, consumer).orElse(defaultConsumer);
    }

    /**
     * 等值判断
     *
     * @param bool
     * @param consumer
     * @return
     */
    public AbstractOptionalService<T> eq(boolean bool, Consumer<T> consumer) {
        if (isNext() && bool) {
            breakNext();
            consumer.accept(getObject());
        }
        return this;
    }

    public void orElse(Consumer<T> consumer) {
        if (isNext()) {
            consumer.accept(getObject());
        }
    }

    public static <T> AbstractOptionalService<T> of(T object) {
        return new AbstractOptionalService<T>(object);
    }

    public void eq(T error, Object o) {

    }

}
