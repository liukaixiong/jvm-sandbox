package com.lkx.jvm.sandbox.core.util;


import com.google.common.collect.Sets;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.compoents.ClassDumpTransformer;
import com.lkx.jvm.sandbox.core.model.common.Pair;
import org.junit.Test;

import java.io.File;
import java.util.Map;
import java.util.NavigableMap;

public class DecompilerUtilsTest {

    @Test
    public void testClassFile() {
        boolean lineNumber = true;
        boolean hideUnicode = false;
        String methodName = null;

        String clazzPath = "D:\\elab\\project\\JVM-Sandbox\\sandbox-manager-core\\target\\classes\\com\\lkx\\jvm\\sandbox\\core\\Constants.class";
        Pair<String, NavigableMap<Integer, Integer>> decompileResult = DecompilerUtils.decompileWithMappings(clazzPath, methodName, hideUnicode, lineNumber);
        System.out.println(decompileResult);
    }

    @Test
    public void testClassForName() {
        boolean lineNumber = true;
        boolean hideUnicode = false;
        String methodName = null;
        Class<Constants> constantsClass = Constants.class;
        ClassDumpTransformer transformer = new ClassDumpTransformer(Sets.newHashSet(constantsClass));

        Map<Class<?>, File> dumpResult = transformer.getDumpResult();
        File classFile = dumpResult.get(constantsClass);

        Pair<String, NavigableMap<Integer, Integer>> decompileResult = DecompilerUtils.decompileWithMappings(classFile.getAbsolutePath(), methodName, hideUnicode, lineNumber);
        System.out.println(decompileResult);
    }
}