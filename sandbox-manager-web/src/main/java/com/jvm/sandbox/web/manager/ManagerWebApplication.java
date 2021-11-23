package com.jvm.sandbox.web.manager;

import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.ruoyi.client.annotation.EnableAVue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/8 - 17:24
 */
@SpringBootApplication
@EnableAVue(basePackages = "com.jvm.sandbox.web.manager.avue", enumsPackagesClasses = {CommandEnums.Debug.class, CommandEnums.Watcher.class})
public class ManagerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerWebApplication.class, args);
    }

}
