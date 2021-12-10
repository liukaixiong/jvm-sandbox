package com.sandbox.demo.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sandbox.demo.example.User;
import com.sandbox.demo.mapper.ApiTestMapper;
import com.sandbox.demo.model.ApiTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/26 - 13:56
 */
@RestController
public class MapperController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ApiTestMapper apiTestMapper;

    @RequestMapping("/getList")
    public List<ApiTest> index() {
        List<ApiTest> apiTests = apiTestMapper.selectList(null);
        return apiTests;
    }

    @RequestMapping("/getObject")
    public List<ApiTest> getObject() {
        QueryWrapper<ApiTest> wrapper = new QueryWrapper<>();
        wrapper.eq("id", 1);
        List<ApiTest> apiTests = apiTestMapper.selectList(wrapper);
        return apiTests;
    }

    @GetMapping(value = "/updateById", produces = "application/json;charset=UTF-8")
    public Map<String, Object> updateById() {
        logger.info("开始触发了updateById方法");
        ApiTest apiTest = new ApiTest();
        apiTest.setId(1);
        apiTest.setValue("lkx-" + System.currentTimeMillis());
        int result = apiTestMapper.updateById(apiTest);
        logger.info("结束触发方法，得到返回值：{} , 其他参数 {} ,{} ,{}", result, "a1", "a2", "a3");
        return trues();
    }

    @GetMapping(value = "/insert", produces = "application/json;charset=UTF-8")
    public Map<String, Object> insert() {
        ApiTest apiTest = new ApiTest();
        apiTest.setValue("insert-" + System.currentTimeMillis());
        apiTestMapper.insert(apiTest);
        return trues();
    }


    public Map<String, Object> trues() {
        Map<String, Object> map = new HashMap<>();
        map.put("sucesss", true);
        return map;
    }

}
