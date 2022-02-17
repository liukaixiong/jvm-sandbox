package com.sandbox.demo.service.impl;

import com.sandbox.demo.service.Demo2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/21 - 14:28
 */
@Service
public class Demo2ServiceImpl implements Demo2Service {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @Transactional
    public String get() {
        logger.info(" 开始执行 [Demo2ServiceImpl#get]方法");
        return "demo2ServiceImpl ok!";
    }
}
