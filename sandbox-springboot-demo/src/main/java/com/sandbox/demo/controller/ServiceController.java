package com.sandbox.demo.controller;

import com.sandbox.demo.model.RS;
import com.sandbox.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/26 - 13:56
 */
@RestController
public class ServiceController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DemoService demoService;

    @RequestMapping("/service/getList")
    public RS getList() {
        logger.debug("开始ServiceController.getList 方法");
        return new RS(demoService.getList());
    }

}
