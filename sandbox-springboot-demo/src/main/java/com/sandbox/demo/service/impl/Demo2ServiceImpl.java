package com.sandbox.demo.service.impl;

import com.sandbox.demo.service.Demo2Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2022/1/21 - 14:28
 */
@Service
public class Demo2ServiceImpl implements Demo2Service {

    @Override
    @Transactional
    public String get() {
        return "demo2ServiceImpl ok!";
    }
}
