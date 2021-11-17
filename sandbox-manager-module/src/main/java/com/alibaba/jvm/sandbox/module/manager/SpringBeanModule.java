package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceAdapterListener;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleManager;
import com.alibaba.jvm.sandbox.module.manager.components.SpringContextContainer;
import com.lkx.jvm.sandbox.core.Constants;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取Spring的上下文内容
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/9 - 16:53
 */
@MetaInfServices(Module.class)
@Information(id = Constants.SPRING_BEAN_MODULE_ID, version = "0.0.1", author = "liukaixiong")
public class SpringBeanModule implements Module, LoadCompleted {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private ModuleEventWatcher watcher;

    @Resource
    private ConfigInfo configInfo;

    @Resource
    private ModuleManager moduleManager;

    @Override
    public void loadCompleted() {
        new EventWatchBuilder(watcher)
                .onClass("org.springframework.context.event.ContextRefreshedEvent")
                .onBehavior("<init>")
                .onWatch(new AdviceAdapterListener(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        try {
                            Object attachment = advice.attachment();
                            Object factory = advice.getParameterArray()[0];
                            SpringContextContainer.getInstance().setApplicationContext(factory);
                            SpringContextContainer.getInstance().setLoad(true);
                            String port = SpringContextContainer.getInstance().getProperties("server.port");
                            String applicationName = SpringContextContainer.getInstance().getProperties("spring.application.name");
                            log.info("应用: " + applicationName + "  端口 : " + port + " 强制获取Spring的上下文环境并注入成功!");
                        } catch (Exception e) {
                            log.error("[Error-2000]-register spring bean occurred error.", e);
                        }
                    }
                }), Event.Type.BEFORE, Event.Type.RETURN);


        // 我想知道Spring的容器启动了多久.
        new EventWatchBuilder(watcher)
                .onClass("org.springframework.context.support.AbstractApplicationContext")
                .onBehavior("refresh")
                .onWatch(new AdviceAdapterListener(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        Map<String, Object> container = new HashMap<>();
                        container.put("startTime", System.currentTimeMillis());
                        log.info("spring 开始构建: ");
                        advice.attach(container);
                    }

                    @Override
                    protected void after(Advice advice) throws Throwable {
                        Map<String, Object> container = advice.attachment();
                        Long startTime = (Long) container.get("startTime");
                        long time = System.currentTimeMillis() - startTime;
                        log.info("当前容器耗时 : " + time);
                    }
                }), Event.Type.BEFORE, Event.Type.RETURN);


    }


}
