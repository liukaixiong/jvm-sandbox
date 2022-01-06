package com.lkx.jvm.sandbox.core.util;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * 条件判断工具类
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/31 - 14:49
 */
public class BooleanOptional<T> {

    public static <T> Optional<T> ofTrue(T value, boolean test) {

        if (Objects.nonNull(value) && test) {
            return Optional.of(value);
        }

        return Optional.empty();
    }

    /**
     * 省略if条件的写法
     *
     * <code>
     * if(list != null && list.size() == 1){
     * return list.get(0);
     * }else{
     * return null;
     * }
     * <p>
     * 简化写法 : BooleanOptional.ofTrue(objectList, objectList.size() == 1, k -> k.get(0))
     *
     *
     * </code>
     *
     * @param object   操作对象
     * @param test     判断条件
     * @param function 返回条件
     * @param <T>
     * @return
     */
    public static <T> T ofObject(T object, boolean test, Function<T, T> function) {
        if (ofTrue(object, test).isPresent()) {
            return function.apply(object);
        }
        return null;
    }

    public static <T> T ofList(List<T> object, boolean test, Function<List<T>, T> function) {
        if (ofTrue(object, test).isPresent()) {
            return function.apply(object);
        }
        return null;
    }

}
