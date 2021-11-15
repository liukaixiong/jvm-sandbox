package com.sandbox.demo.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/10 - 18:39
 */
public class Size {

    private List<String> list = new ArrayList<>();
    private Map<Integer, Integer> map = new HashMap<>();

    public void run(Integer num) {
        for (int i = 0; i < num; i++) {
            list.add(i+"");
            map.put(i,i);
        }
    }

}
