package com.alibaba.jvm.sandbox.module.manager.debug;

import com.google.common.collect.Sets;
import com.lkx.jvm.sandbox.core.Constants;
import com.lkx.jvm.sandbox.core.compoents.ClassDumpTransformer;
import com.lkx.jvm.sandbox.core.enums.CommandEnums;
import com.lkx.jvm.sandbox.core.model.command.CommandDebugModel;
import com.lkx.jvm.sandbox.core.model.common.Pair;
import com.lkx.jvm.sandbox.core.model.response.JsonResult;
import com.lkx.jvm.sandbox.core.util.DecompilerUtils;
import com.lkx.jvm.sandbox.core.util.InstrumentationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;

/**
 * jad命令实现
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/22 - 11:30
 */
public class JadCommandProcess implements CommandDebugProcess<String> {

    @Override
    public CommandEnums.Debug command() {
        return CommandEnums.Debug.jad;
    }

    @Override
    public String invoke(Instrumentation inst, CommandDebugModel req) throws Exception {
        boolean lineNumber = true;
        boolean hideUnicode = false;
        String methodName = null;
        String classNamePattern = req.getClassNamePattern();
        HashSet<Class<?>> allClasses = Sets.newHashSet();
        Class<?> clazz = Class.forName(classNamePattern);
        allClasses.add(clazz);
        ClassDumpTransformer transformer = new ClassDumpTransformer(allClasses);
        InstrumentationUtils.retransformClasses(inst, transformer, allClasses);
        Map<Class<?>, File> dumpResult = transformer.getDumpResult();
        File classFile = dumpResult.get(clazz);
        Pair<String, NavigableMap<Integer, Integer>> decompileResult = DecompilerUtils.decompileWithMappings(classFile.getAbsolutePath(), methodName, hideUnicode, lineNumber);
        return decompileResult.getFirst();
    }
}
