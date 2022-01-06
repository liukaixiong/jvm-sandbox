package com.lkx.jvm.sandbox.core.factory;

import com.lkx.jvm.sandbox.core.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jar包的管理
 * <p>
 * 这里有非常多的点需要考虑:
 * 1. 你加载的jar是一个独立的ClassLoader，因为每个jar都持有一部分对象，那么就意味着，你需要维护这个ClassLoader，尽可能保证这里面的对象不要被其他地方引用到。
 * 2. 有加入,那么修改呢? 修改的时候需要干掉之前加入的jar的ClassLoader。干掉之前还需要考虑这个classloader加载的bean有没有被其他对象给引用到，比如缓存。
 * 3. 有加入、修改还得有卸载啊！卸载的时候和这个ClassLoader相关的对象都得干掉。
 * <p>
 * JVM-sandbox之前的插件都是无状态的。生成的对象都被包装成了CoreModule，更关键的是这个对象只能被DefaultCoreModuleManager给操作，除了核心改源码，基本上碰不到这个类，防御的很好。
 * 所以如果要达到它这样的效果，这里面的对象必须要被管理起来。它的卸载都是基于插件编号，一个个加载一个个卸载的。要么就是全部卸载，全部卸载又只需要遍历持有CoreModule缓存就行了。
 * 具体参考 {@link com.alibaba.jvm.sandbox.core.manager.impl.DefaultCoreModuleManager#unload(com.alibaba.jvm.sandbox.core.CoreModule, boolean)}
 * <p>
 * 实现的时候要思考,到底是基于jar的纬度，还是像sandbox那样基于模块编号。
 * <p>
 * 基于Jar的纬度会更好,要么应用要么不应用。可以覆盖。就不再像模块编号那么细粒度，所以尽可能把jar的职责规划好。
 * 我们首要目标是设计一个多维度的缓存。
 * File -》 ClassLoader -》
 *
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/9 - 13:47
 */
public class JarManager {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Map<String, Long> jarFileSignCache = new HashMap<>();

    private final static String JAR_FILE_SUFFIX = ".jar";

    public List<File> loadJarObjectFile(String jarFilePath) {

        File file = new File(jarFilePath);

        if (!file.exists() && !file.mkdirs()) {
            throw new IllegalArgumentException("jar file does not exist, path=" + jarFilePath);
        }

        List<File> jarFileList = new ArrayList<>();

        processValidFile(file, jarFileList);

        return jarFileList;
    }

    /**
     * 检查是否被加载过
     *
     * @param file
     * @return
     */
    private boolean checkLoaded(File file) {
        String key = file.getName();
        Long jarVersion = jarFileSignCache.get(key);
        if (jarVersion == null || jarVersion == 0) {
            long currentJarVersion = checksumCRC32(file);
            jarFileSignCache.put(key, currentJarVersion);
            return false;
        } else {
            long currentJarVersion = checksumCRC32(file);
            return jarVersion == currentJarVersion;
        }
    }

    private void processValidFile(File file, List<File> jarPaths) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File jarFile : files) {
                processValidFile(jarFile, jarPaths);
            }
        } else {
            if (isJar(file)) {

                if (checkLoaded(file)) {
                    log.info("该文件已被加载过:" + file.getName());
                    return;
                }

                jarPaths.add(file);
            }
        }
    }

    private long checksumCRC32(File file) {
        try {
            return FileUtils.checksumCRC32(file);
        } catch (IOException e) {
            log.error("check file error", e);
        }
        return 0;
    }

    /**
     * @param file
     * @return
     */
    private boolean isJar(File file) {
        return file.isFile() && file.getName().endsWith(JAR_FILE_SUFFIX);
    }

}
