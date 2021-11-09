package com.sandbox.demo;

import com.sandbox.demo.example.Clock;
import com.sandbox.demo.example.Trace;
import com.sandbox.demo.example.Watch;
import com.sandbox.demo.utils.Utils;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/2 - 16:32
 */
public class Main {


    public static void asyncRun(Consumer consumer) {
        CompletableFuture.supplyAsync(() -> {
            while (true) {
                consumer.accept(null);
                Utils.sleep(1000);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        asyncRun((a) -> new Clock().loopReport());
        asyncRun((a) -> new Watch().run("123123"));
        asyncRun((a) -> new Trace().run("555555"));
        System.in.read();
    }
}
