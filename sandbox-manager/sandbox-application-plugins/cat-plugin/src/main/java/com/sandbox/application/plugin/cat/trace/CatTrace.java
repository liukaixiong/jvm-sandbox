package com.sandbox.application.plugin.cat.trace;

import com.sandbox.manager.api.MethodAdviceInvoke;
import com.sandbox.manager.api.Trace;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/17 - 17:25
 */
@Component
public class CatTrace implements Trace {

    @Autowired
    private List<MethodAdviceInvoke> methodAdviceInvokes;

    @Override
    public String getId() {
        String id = "cat-" + RandomUtils.nextInt(10000, 99999) + "";
        System.out.println("cat id 获取:" + id);
        methodAdviceInvokes.forEach(System.out::println);
        return id;
    }
}
