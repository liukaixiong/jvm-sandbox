package com.sandbox.application.plugin.cat.event;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.lkx.jvm.sandbox.core.compoents.PrintFormat;

/**
 * 日志埋点事件
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/29 - 10:56
 */
public class LogEvent {
    public static void process(ModuleEventWatcher watcher) {
        // UPDATE
        new EventWatchBuilder(watcher)
                .onClass("org.slf4j.Logger")
                .includeSubClasses()
                .onBehavior("info")
                .onBehavior("debug")
                .onWatch(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        if (!advice.isProcessTop()) {
                            return;
                        }
                        Object[] parameterArray = advice.getParameterArray();
                        Object msg = parameterArray[0];
                        if (msg instanceof String) {
                            String content = msg.toString();
                            if (parameterArray.length > 1) {
                                Object logParams = advice.getParameterArray()[1];
                                if (logParams instanceof Object[]) {
                                    Object[] logParameterArray = (Object[]) logParams;
                                    for (int i = 0; i < logParameterArray.length; i++) {
                                        Object o = logParameterArray[i];
                                        content = content.replaceFirst("\\{}", o.toString());
                                    }
                                }
                            }
                            PrintFormat format = new PrintFormat();
                            format.put("打印事件", "LOG");
                            format.put("日志内容", content);
                            format.printLog();
                        }
                    }
                });
    }
}
