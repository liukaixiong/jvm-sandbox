package com.sandbox.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/2 - 16:58
 */
@SpringBootApplication
@MapperScan("com.sandbox.demo.mapper")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
