package com.sandbox.demo.controller;

import com.sandbox.demo.config.prop.PluginProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private Environment environment;

    @Autowired
    private PluginProperties pluginProperties;

    @RequestMapping("/")
    public String index() {
        return "hello, there";
    }
}