package com.sandbox.application.plugin.cat.event;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.lkx.jvm.sandbox.core.compoents.PrintFormat;

/**
 * mybatis的Event事件
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/26 - 18:33
 */
@Deprecated
public class MapperEvent {
    public static void process(ModuleEventWatcher watcher) {
        // UPDATE
        new EventWatchBuilder(watcher)
                .onClass("org.apache.ibatis.session.defaults.DefaultSqlSession")
                .includeSubClasses()
                .onBehavior("update")
                .onWatch(new AdviceListener() {

                    @Override
                    protected void before(Advice advice) throws Throwable {
                        if (!advice.isProcessTop()) {
                            return;
                        }
                        // 记录耗时
                        advice.getProcessTop().attach(System.currentTimeMillis());
                    }


                    @Override
                    protected void after(Advice advice) throws Throwable {
                        if (!advice.isProcessTop()) {
                            return;
                        }
                        String statement = advice.getParameterArray()[0].toString();
                        // 记录成功或者失败
                        int success = 1;
                        if (advice.isThrows()) {
                            success = -1;
                        }
                        Object returnObj = advice.getReturnObj();
                        Long start = advice.getProcessTop().attachment();
                        Long time = System.currentTimeMillis() - start;

                        PrintFormat format = new PrintFormat();
                        format.put("名称", statement);
                        format.put("执行结果", success);
                        format.put("返回参数", returnObj);
                        format.put("耗时", time);
                        format.printLog();
                    }
                });
    }
}
