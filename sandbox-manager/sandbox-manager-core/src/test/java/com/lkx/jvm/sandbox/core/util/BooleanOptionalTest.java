package com.lkx.jvm.sandbox.core.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BooleanOptionalTest {

    public void testOfTrue() {
    }

    @Test
    public void testTestOfTrue() {

        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        String s = BooleanOptional.ofList(list, list.size() == 1, k -> k.get(0));

        System.out.println(s);
    }
}