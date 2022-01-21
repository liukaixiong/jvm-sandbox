package com.sandbox.application.plugin.cat;

import com.dianping.cat.Cat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/18 - 11:29
 */
@Component
public class CatInitializeApplication implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("开始初始化cat的上下文环境");
        /*
            说明一下为什么要有这行代码:
              1. 由于插件是多加载器实现的，你如果在初始化的时候没有指定当前线程的类加载器，在CAT初始化的时候，默认会找当前线程上下文的加载器。
              它是这么找的Thread.currentThread().getContextClassLoader(),这是它的容器查找规则具体可以参考: org.codehaus.plexus.ClassRealmUtil.getContextRealms
              当时也是花了很大力气,走了很多弯路,才发现. 只要在初始化之前将加载器指定好是直接加载CAT的jar的加载器就不会出现问题,加载完成之后便会直接缓存起来,不会影响下一次运行.
              所以你只需要保证这个方法只执行一次,并且是在cat初始化之前
         */
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(CatInitializeApplication.class.getClassLoader());
        // 初始化
        Cat.getManager();
        // 还是还原一下原本上下文的信息,避免因为这一次cat的重启导致不必要的问题.
        Thread.currentThread().setContextClassLoader(contextClassLoader);
        logger.info("cat的上下文环境初始化完成...");
    }
}
