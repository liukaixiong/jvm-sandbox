package com.sandbox.demo.controller;

import com.sandbox.demo.example.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/2 - 17:00
 */
@RestController
public class DemoController {

    @GetMapping(value = "/maxSize", produces = "application/json;charset=UTF-8")
    public Map<String, Object> maxSize(@RequestParam("number") String number) {
        new Size().run(Integer.valueOf(number));
        return trues();
    }

    @GetMapping(value = "/stack", produces = "application/json;charset=UTF-8")
    public Map<String, Object> stack(@RequestParam("number") Integer number) {
        new Stack().run(number);
        return trues();
    }

    @RequestMapping(value = "/trace", produces = "application/json;charset=UTF-8")
    public Map<String, Object> trace(@RequestParam("body") String body) {
        new Trace().run(body);
        return trues();
    }

    @GetMapping(value = "/watch", produces = "application/json;charset=UTF-8")
    public Map<String, Object> watch(@RequestParam("body") String body) {
        new Watch().run(body);
        return trues();
    }

    @GetMapping(value = "/clock", produces = "application/json;charset=UTF-8")
    public Map<String, Object> clock(@RequestParam("body") String body) {
        new Clock().loopReport();
        return trues();
    }

    @GetMapping(value = "/user", produces = "application/json;charset=UTF-8")
    public Map<String, Object> user(@RequestParam("body") String body) {
        new User().getUser(body);
        return trues();
    }


    public Map<String, Object> trues() {
        Map<String, Object> map = new HashMap<>();
        map.put("sucesss", true);
        return map;
    }

}
