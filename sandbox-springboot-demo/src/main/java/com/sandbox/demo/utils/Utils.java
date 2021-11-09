package com.sandbox.demo.utils;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/2 - 16:40
 */
public class Utils {

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
