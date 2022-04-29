package com.sandbox.application.plugin.cat.components.trace;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @Module 异步线程
 * @Description
 * @Author liukaixiong
 * @Date 2021/1/22 15:55
 */
public class CatSupplier<T> implements Supplier<T>, Runnable {

    private Supplier<T> supplier;

    private Runnable runnable;

    private Map<String, String> msgContextMap;

    private final String name = "supplier_thread";

    public CatSupplier(Supplier<T> supplier) {
        this.msgContextMap = CatCrossProcess.getMsgContextMap();
        this.supplier = supplier;
    }

    public CatSupplier(Runnable runnable) {
        this.msgContextMap = CatCrossProcess.getMsgContextMap();
        this.runnable = runnable;
    }

    @Override
    public T get() {
        return CatCrossProcess.buildRemoteMsg(CatMsgConstants.LOCAL_THREAD_SERVER, name, this.msgContextMap, () -> {
            return this.supplier.get();
        });
    }

    @Override
    public void run() {
        CatCrossProcess.buildRemoteMsg(CatMsgConstants.LOCAL_THREAD_SERVER, name, this.msgContextMap, () -> {
            this.runnable.run();
            return null;
        });
    }
}
