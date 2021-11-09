package com.sandbox.demo.example;

/**
 * 报时的钟
 */
public class Clock {

    // 日期格式化
    private final java.text.SimpleDateFormat clockDateFormat
            = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 状态检查 : 注意这里是否返回一个异常
     */
    final void checkState() {
        throw new IllegalStateException("STATE ERROR!");
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    final java.util.Date now() {
        return new java.util.Date();
    }

    /**
     * 报告时间
     *
     * @return 报告时间
     */
    final String report() {
        checkState();
        return clockDateFormat.format(now());
    }

    /**
     * 循环播报时间
     */
    public final void loopReport() {
//        while (true) {
        try {
            System.out.println(report());
        } catch (Throwable cause) {
            cause.printStackTrace();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        }
    }

    public static void main(String... args) throws InterruptedException {
        new Clock().loopReport();
    }

}