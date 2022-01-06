package com.sandbox.application.plugin.cat.listener;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.lkx.jvm.sandbox.core.compoents.PrintFormat;
import com.sandbox.manager.api.AdviceNameDefinition;
import com.sandbox.manager.api.MethodAdviceInvoke;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 动态日志的植入
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/8 - 17:25
 */
@Component
public class LogAdviceListener implements MethodAdviceInvoke , ApplicationContextAware {

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
    public void before(Advice advice) throws Throwable {
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
}
