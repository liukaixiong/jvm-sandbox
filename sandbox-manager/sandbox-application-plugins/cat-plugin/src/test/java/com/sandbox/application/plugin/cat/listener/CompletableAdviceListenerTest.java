package com.sandbox.application.plugin.cat.listener;

import junit.framework.TestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableAdviceListenerTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private ExecutorService executorService = Executors.newFixedThreadPool(5);


    @Test
    public void testBefore() throws InterruptedException {

//        executorService.execute(()->{
//            System.out.println("==================runnable");
//        });
//        System.out.println("kaishi ");
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("==================runnable");
//            }
//        };
//        logger.info("runable ： "+runnable);
        CompletableFuture.supplyAsync(()->{
            System.out.println("------>>>>>>成功！");
            return null;
        });
    }
}