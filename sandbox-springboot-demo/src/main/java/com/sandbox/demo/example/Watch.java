package com.sandbox.demo.example;

/**
 * 用来观察watch方法
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/2 - 16:32
 */
public class Watch {

    public void run(String body) {
        A(body);
    }

    public void A(String body) {
        B(body);
    }

    public void B(String body) {
        C(body);
    }

    public void C(String body) {
        System.out.println("WatchDemo : 执行完毕 : " + body);
    }

}
