package com.lkx.jvm.sandbox.core.compoents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 链路详情处理类
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/13 - 15:28
 */
public class TraceInfoHelper {

    private static Logger logger = LoggerFactory.getLogger(TraceInfoHelper.class);

    private static ThreadLocal<Map<String, Object>> threadLocal = ThreadLocal.withInitial(() -> new LinkedHashMap<>(50));

    public static void addData(String key, Object value) {
        Map<String, Object> map = threadLocal.get();
        Object oldData = map.put(key, value);
        if (oldData != null) {
            logger.warn("threadLocal set value exist : " + key);
        }
    }

    public static Object getData(String key) {
        return threadLocal.get().get(key);
    }

    public static Map<String, Object> getAllData() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
