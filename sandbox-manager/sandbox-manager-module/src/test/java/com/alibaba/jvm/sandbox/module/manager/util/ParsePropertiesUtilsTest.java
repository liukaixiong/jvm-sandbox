package com.alibaba.jvm.sandbox.module.manager.util;

import com.alibaba.jvm.sandbox.module.manager.model.PluginProperties;
import com.alibaba.jvm.sandbox.module.manager.util.model.ComplexModel;
import com.alibaba.jvm.sandbox.module.manager.util.model.UserModel;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ParsePropertiesUtilsTest {


    @Test
    public void testPluginProperties() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("spring.sandbox.plugin.register.cat[0].classPattern", "com.sandbox.demo.controller.*");
        config.put("spring.sandbox.plugin.register.cat[0].methodNames", "*");
        config.put("spring.sandbox.plugin.register.cat[0].adviceNames[0]", "PRINT_LOG");
        config.put("spring.sandbox.plugin.register.cat[0].adviceNames[1]", "CAT_TRANSACTION_POINT");
        config.put("spring.sandbox.plugin.register.cat[1].classPattern", "com.sandbox.demo.controller.*");
        config.put("spring.sandbox.plugin.register.cat[1].methodNames", "*");
        config.put("spring.sandbox.plugin.register.cat[1].adviceNames[0]", "print_log");
        config.put("spring.sandbox.plugin.register.cat[1].adviceNames[1]", "CAT_TRANSACTION_POINT");
        PluginProperties pluginProperties = ParsePropertiesUtils.parseObject("spring.sandbox.plugin", "cat", config::get, PluginProperties.class);
        System.out.println(pluginProperties);
    }

    @Test
    public void testUserModel() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("spring.sandbox.plugin.user.username", "liukx");
        config.put("spring.sandbox.plugin.user.password", "123");
        config.put("spring.sandbox.plugin.user.age", "33");
        UserModel userModel = ParsePropertiesUtils.parseObject("spring.sandbox.plugin.user", null, config::get, UserModel.class);
        System.out.println(userModel);
    }

    @Test
    public void testComplexModel() throws Exception {
        Map<String, String> config = new HashMap<>();
        config.put("spring.sandbox.plugin.user.username", "liukx");
        config.put("spring.sandbox.plugin.user.password", "123");
        config.put("spring.sandbox.plugin.user.age", "33");

        //List<UserModel> userModel;
        config.put("spring.sandbox.plugin.user.userModel[0].username", "liukx_0");
        config.put("spring.sandbox.plugin.user.userModel[0].password", "123");
        config.put("spring.sandbox.plugin.user.userModel[0].age", "33");
        config.put("spring.sandbox.plugin.user.userModel[1].username", "liukx_1");
        config.put("spring.sandbox.plugin.user.userModel[1].password", "123");
        config.put("spring.sandbox.plugin.user.userModel[1].age", "33");

        // Map<String, UserModel> maoUserModel;
        config.put("spring.sandbox.plugin.user.maoUserModel.abc.username", "liukx_0");
        config.put("spring.sandbox.plugin.user.maoUserModel.abc.password", "123");
        config.put("spring.sandbox.plugin.user.maoUserModel.abc.age", "33");
        config.put("spring.sandbox.plugin.user.maoUserModel.cdf.username", "liukx_1");
        config.put("spring.sandbox.plugin.user.maoUserModel.cdf.password", "123");
        config.put("spring.sandbox.plugin.user.maoUserModel.cdf.age", "33");

        // Map<String, List<UserModel>>
        config.put("spring.sandbox.plugin.user.stringListMap.abc[0].username", "liukx_0");
        config.put("spring.sandbox.plugin.user.stringListMap.abc[0].password", "123");
        config.put("spring.sandbox.plugin.user.stringListMap.abc[0].age", "33");
        config.put("spring.sandbox.plugin.user.stringListMap.cdf[1].username", "liukx_1");
        config.put("spring.sandbox.plugin.user.stringListMap.cdf[1].password", "123");
        config.put("spring.sandbox.plugin.user.stringListMap.cdf[1].age", "33");


        ComplexModel userModel = ParsePropertiesUtils.parseObject("spring.sandbox.plugin.user", "abc", config::get, ComplexModel.class);
        System.out.println(userModel);
    }

}