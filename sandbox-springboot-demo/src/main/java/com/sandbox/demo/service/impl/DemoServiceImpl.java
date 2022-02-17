package com.sandbox.demo.service.impl;

import com.sandbox.demo.mapper.ApiTestMapper;
import com.sandbox.demo.model.ApiTest;
import com.sandbox.demo.service.Demo2Service;
import com.sandbox.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/21 - 14:04
 */
@Service
public class DemoServiceImpl implements DemoService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ApiTestMapper apiTestMapper;

    @Autowired
    private Demo2Service demo2Service;

    @Override
    public List<ApiTest> getList() {
        logger.debug("开始DemoService.getList 方法 ");
        logger.debug("测试toString方法 : {}", this.toString());
        logger.debug("测试hashCode方法 : {}", this.hashCode());
        logger.debug("测试testNormalMethod方法 : {}", this.testNormalMethod());
        logger.debug("测试testPrivateMethod方法 : {}", this.testPrivateMethod());
        logger.debug("测试testProtectedMethod方法 : {}", this.testProtectedMethod());
        logger.debug("测试Demo2Service.get方法 : {}", demo2Service.get());
        return apiTestMapper.selectList(null);
    }

    public String testNormalMethod() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("正在执行testNormalMethod方法");
        return "testNormalMethod OK!";
    }

    private String testPrivateMethod() {
        logger.info("正在执行testPrivateMethod方法");
        return "testPrivateMethod OK!";
    }

    protected String testProtectedMethod() {
        logger.info("正在执行testProtectedMethod方法");
        return "testProtectedMethod OK!";
    }

}
