package com.sandbox.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LogController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log/info")
    public Map<String, Object> info() {
        logger.info("这是info信息");
        logger.info("这是info带参数的信息 : {}", "哈哈");
        logger.info("这是info带参数的信息 : {} , {}", "哈哈", "呵呵");
        return result();
    }


    @RequestMapping("/log/error")
    public Map<String, Object> error() {
        logger.info("开始模拟异常");
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            logger.error("模拟异常", e);
        }
        logger.warn("异常模拟结束");
        return result();
    }

    private Map<String, Object> result() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("message", "访问成功!");
        return map;
    }
}