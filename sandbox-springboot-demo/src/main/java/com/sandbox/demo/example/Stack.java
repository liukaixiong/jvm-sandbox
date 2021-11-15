package com.sandbox.demo.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/11 - 10:31
 */
public class Stack {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public void run(Integer number) {
        logger.info("进入gc被触发方法:" + number);
        gc(number);
    }

    public void gc(Integer number) {
        for (int i = 0; i < number; i++) {
            System.gc();
        }
    }


}
