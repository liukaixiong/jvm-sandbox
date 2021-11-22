package com.sandbox.demo.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/22 - 14:39
 */
public class GetStatic {
    private static String userName = "liukx";
    private static List<String> stringList = new ArrayList<>();
    private static QueueModel queueModel = new QueueModel();

    static {
        for (int i = 0; i < 10; i++) {
            queueModel.add("tttt__" + i);
            stringList.add("str_" + i);
        }
    }


    static class QueueModel {
        private static Queue queue = new ArrayBlockingQueue<>(100);

        public static void add(String text) {
            queue.add(text);
        }

        public int size() {
            return queue.size();
        }
    }

}
