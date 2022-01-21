package com.sandbox.application.plugin.cat.utils;

/**
 * 范围工具
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/19 - 16:25
 */
public class RangeUtils {

    /**
     * 将一个数值归类到一个范围区间
     *
     * @param number
     * @param rangeNumber
     * @return
     */
    public static Integer getRangeNumber(Integer number, Integer rangeNumber) {
        return Math.floorDiv(number, rangeNumber) * rangeNumber;
    }
}
