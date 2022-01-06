package com.sandbox.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/2 - 16:58
 */
@SpringBootApplication
@MapperScan("com.sandbox.demo.mapper")
public class DemoApplication {

    /**
     * 运行参数配置 :
     * -javaagent:E:\\study\sandbox\\lib\\sandbox-agent-1.3.3-jar-with-dependencies.jar
     * <p>
     * 如果遇到:
     * Failed to register LiveBeansView MBean; nested exception is javax.management.InstanceAlreadyExistsException: DefaultDomain:application=
     * javax.management.InstanceAlreadyExistsException: DefaultDomain:application=
     * 是IDEA的运行配置中 [Run/debug -> 选择你运行的启动配置 -> 取消掉Enable JMX Agent]
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
