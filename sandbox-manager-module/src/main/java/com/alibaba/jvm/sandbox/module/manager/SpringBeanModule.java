package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.LoadCompleted;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceAdapterListener;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.module.manager.components.SpringContextContainer;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.factory.GlobalFactoryHelper;
import com.sandbox.manager.api.SpringLoadCompleted;
import org.apache.commons.collections4.CollectionUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Consumer;

/**
 * 获取Spring的上下文内容
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/9 - 16:53
 */
@MetaInfServices(Module.class)
@Information(id = Constants.SPRING_BEAN_MODULE_ID, version = "0.0.1", author = "liukaixiong", mode = Information.Mode.AGENT)
public class SpringBeanModule implements Module, LoadCompleted {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private ModuleEventWatcher watcher;

    @Override
    public void loadCompleted() {
        new EventWatchBuilder(watcher)
                .onClass("org.springframework.context.event.ContextRefreshedEvent")
                .onBehavior("<init>")
                .onWatch(new AdviceAdapterListener(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        try {
                            Object factory = advice.getParameterArray()[0];
                            SpringContextContainer.getInstance().setApplicationContext(factory);
                            SpringContextContainer.getInstance().setLoad(true);
                            String port = SpringContextContainer.getInstance().getProperties("server.port");
                            String applicationName = SpringContextContainer.getInstance().getProperties("spring.application.name");
                            log.info("应用: " + applicationName + "  端口 : " + port + " 强制获取Spring的上下文环境并注入成功!");
                            // 这个时候需要回调容器启动方法.
                            springLoadCompletedCallback(SpringLoadCompleted::refreshCallback);
                        } catch (Exception e) {
                            springLoadCompletedCallback(SpringLoadCompleted::errorCallback);
                            log.error("[Error-2000]-register spring bean occurred error.", e);
                        } finally {
                            springLoadCompletedCallback(SpringLoadCompleted::callback);
                        }
                    }
                }), Event.Type.BEFORE, Event.Type.RETURN);
    }

    private void springLoadCompletedCallback(Consumer<SpringLoadCompleted> consumer) {
        List<SpringLoadCompleted> springLoadCompletedList = GlobalFactoryHelper.getInstance().getList(SpringLoadCompleted.class);
        if (!CollectionUtils.isEmpty(springLoadCompletedList)) {
            for (SpringLoadCompleted springLoadCompleted : springLoadCompletedList) {
                consumer.accept(springLoadCompleted);
            }
        }
    }

}
