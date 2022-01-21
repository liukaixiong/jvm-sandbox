package com.sandbox.application.plugin.cat.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.lkx.jvm.sandbox.core.util.Strings;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.MethodAdviceInvoke;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 动态日志的植入
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/8 - 17:25
 */
@Component
public class LogAdviceListener implements MethodAdviceInvoke, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public AdviceNameDefinition featureName() {
        return AdviceNameDefinition.PRINT_LOG;
    }

    @Override
    public boolean preHandler(Advice advice) {
        return advice.isProcessTop() && Cat.getManager().isCatEnabled() && Cat.getManager().hasContext() && advice.getParameterArray().length > 0;
    }

    @Override
    public void before(Advice advice) throws Throwable {

        String name = advice.getBehavior().getName();
        Object[] parameterArray = advice.getParameterArray();
        if (parameterArray.length > 0) {
            String msg = parameterArray[0].toString();
            String content = msg.concat("");
            if (parameterArray.length > 1) {
                // Arrays.copyOfRange(parameterArray, 1, parameterArray.length)
                Object arrayValue = parameterArray[1];
                if (arrayValue instanceof Object[]) {
                    content = Strings.lenientFormat(content, "{}", (Object[]) arrayValue);
                } else {
                    content = Strings.lenientFormat(content, "{}", arrayValue);
                }
            }

//            PrintFormat format = new PrintFormat();
//            format.put("打印事件", "LOG");
//            format.put("级别", name);
//            format.put("日志内容", content);
//            format.printLog();

            if ("error".equals(name)) {
                Arrays.stream(parameterArray).filter((param) -> param instanceof Throwable).findFirst().ifPresent((throwable) -> {
                    Cat.logError(throwable.toString(), (Throwable) throwable);
                });
            } else {
                Cat.logEvent("LOG", name, Event.SUCCESS, content);
            }
        }

    }
}
