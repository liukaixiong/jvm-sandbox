package com.sandbox.demo.example;

import com.sandbox.demo.utils.Utils;

/**
 * 用来观察trace方法,该案例仅仅只是为了观察耗时，观察的入口是C方法
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/2 - 16:32
 */
public class Trace {

    public void run(String body) {
        A(body);
    }

    public void A(String body) {
        Utils.sleep(300);
        B(body);
    }

    public void B(String body) {
        Utils.sleep(200);
        C(body);
    }

    public void C(String body) {
        Utils.sleep(100);
        System.out.println("Trace : 执行完毕 : " + body);
    }

}
