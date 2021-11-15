package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.module.manager.components.textui.TTree;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/10 - 18:25
 */
@MetaInfServices(Module.class)
@Information(id = "demo-example", version = "0.0.1", author = "liukaixiong")
public class DemoModule implements Module, LoadCompleted {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ThreadLocal<Long> timeLocal = new InheritableThreadLocal<>();

    @Resource
    private ModuleEventWatcher watcher;

    @Override
    public void loadCompleted() {
        // 开启trace功能
        // trace("com.sandbox.demo.example.*", "*");

//        new EventWatchBuilder(watcher)
//                .onClass(List.class).includeBootstrap()
//                .includeSubClasses()
//                .onBehavior("add")
//                .onWatching()
//                .withCall()
//                .onWatch(new AdviceListener() {
//
//                    @Override
//                    protected void before(Advice advice) throws Throwable {
//                        if (advice.getTarget() != null && advice.getTarget() instanceof List) {
//                            Object target = advice.getTarget();
//                            int size = 0;
//                            try {
//                                System.out.println(advice.getTarget().toString());
//                                size = (int) MethodUtils.invokeMethod(target, "size");
//                            } catch (Exception e) {
//                                System.out.println(e.getMessage());
//                            }
//                            if (size > 10) {
//                                log.info("逮到你拉: " + size);
//                            }
//
//                        }
//                    }
//
//                    @Override
//                    protected void beforeCall(Advice advice, int callLineNum, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {
//                    }
//                });
    }

    private void trace(String classPattern, String methodPattern) {
        new EventWatchBuilder(watcher)
                .onClass(classPattern).includeSubClasses()
                .onBehavior(methodPattern)
                .onWatching()
                .withCall()
                .onWatch(new AdviceListener() {

                    private String getTracingTitle(final Advice advice) {
                        return "Tracing for : "
                                + advice.getBehavior().getDeclaringClass().getName()
                                + "."
                                + advice.getBehavior().getName()
                                + " by "
                                + Thread.currentThread().getName()
                                ;
                    }

                    private String getEnterTitle(final Advice advice) {
                        return "Enter : "
                                + advice.getBehavior().getDeclaringClass().getName()
                                + "."
                                + advice.getBehavior().getName()
                                + "(...);"
                                ;
                    }

                    @Override
                    protected void before(Advice advice) throws Throwable {
                        final TTree tTree;
                        if (advice.isProcessTop()) {
                            timeLocal.set(System.currentTimeMillis());
                            advice.attach(tTree = new TTree(true, getTracingTitle(advice)));
                        } else {
                            tTree = advice.getProcessTop().attachment();
                        }
                        tTree.begin(getEnterTitle(advice));
                    }

                    @Override
                    protected void afterReturning(Advice advice) throws Throwable {
                        final TTree tTree = advice.getProcessTop().attachment();
                        tTree.end();

                        finish(advice);
                    }

                    @Override
                    protected void afterThrowing(Advice advice) throws Throwable {
                        final TTree tTree = advice.getProcessTop().attachment();
                        tTree.begin("throw:" + advice.getThrowable().getClass().getName() + "()").end();
                        tTree.end();
                        finish(advice);
                    }

                    private void finish(Advice advice) {
                        if (advice.isProcessTop()) {
                            long time = System.currentTimeMillis() - timeLocal.get();
                            final TTree tTree = advice.attachment();
                            log.info(tTree.rendering());
                            log.info("当前检测耗时:" + time);
                            timeLocal.remove();
                        }
                    }

                    @Override
                    protected void beforeCall(final Advice advice,
                                              final int callLineNum,
                                              final String callJavaClassName,
                                              final String callJavaMethodName,
                                              final String callJavaMethodDesc) {
                        final TTree tTree = advice.getProcessTop().attachment();
                        tTree.begin(callJavaClassName + ":" + callJavaMethodName + "(@" + callLineNum + ")");
                    }

                    @Override
                    protected void afterCallReturning(final Advice advice,
                                                      final int callLineNum,
                                                      final String callJavaClassName,
                                                      final String callJavaMethodName,
                                                      final String callJavaMethodDesc) {
                        final TTree tTree = advice.getProcessTop().attachment();
                        tTree.end();
                    }

                    @Override
                    protected void afterCallThrowing(final Advice advice,
                                                     final int callLineNum,
                                                     final String callJavaClassName,
                                                     final String callJavaMethodName,
                                                     final String callJavaMethodDesc,
                                                     final String callThrowJavaClassName) {
                        final TTree tTree = advice.getProcessTop().attachment();
                        tTree.set(tTree.get() + "[throw " + callThrowJavaClassName + "]").end();
                    }

                });
    }

    private String getTracingTitle(final Advice advice) {
        return "Tracing for : "
                + advice.getBehavior().getDeclaringClass().getName()
                + "."
                + advice.getBehavior().getName()
                + " by "
                + Thread.currentThread().getName()
                ;
    }

    private String getEnterTitle(final Advice advice) {
        return "Enter : "
                + advice.getBehavior().getDeclaringClass().getName()
                + "."
                + advice.getBehavior().getName()
                + "(...);"
                ;
    }
}
