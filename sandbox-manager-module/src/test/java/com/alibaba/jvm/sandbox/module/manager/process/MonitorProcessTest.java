package com.alibaba.jvm.sandbox.module.manager.process;

import com.lkx.jvm.sandbox.core.model.command.CommandWatcherInfoModel;
import org.apache.commons.lang3.RandomUtils;

public class MonitorProcessTest {

    public static void main(String[] args) throws Throwable {
        Long total = 10l;
        CommandWatcherInfoModel watcherInfoModel = new CommandWatcherInfoModel();
        watcherInfoModel.setTimeOut(total);

        MonitorProcess process = new MonitorProcess(watcherInfoModel);


        for (int j = 0; j < total; j++) {


            for (int i = 0; i < 100; i++) {
                new Thread(() -> {
                    try {
                        process.before(null);

                        int invokeTime = RandomUtils.nextInt(1, 500);

                        Thread.sleep(invokeTime);

                        process.after(null);
                    } catch (Throwable throwable) {
                        System.out.println("error : " + throwable.getMessage());
                    }
                }).start();
            }

            Thread.sleep(999);
            System.out.println(j);
        }

        process.stop();
//        Advice advice = new Advice();
//        process.before();

    }

}