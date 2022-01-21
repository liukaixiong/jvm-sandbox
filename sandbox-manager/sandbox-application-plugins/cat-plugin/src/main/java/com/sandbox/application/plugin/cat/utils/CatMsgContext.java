package com.sandbox.application.plugin.cat.utils;

import com.dianping.cat.Cat;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : liukx
 * @create : 2018/6/25 13:41
 * @email : liukx@elab-plus.com
 */
public class CatMsgContext implements Cat.Context {

    private Map<String, String> properties = new HashMap<>();

    @Override
    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public String getProperty(String key) {
        return properties.get(key);
    }
}
