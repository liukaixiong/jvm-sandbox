package com.sandbox.application.plugin.cat.listener;

import com.dianping.cat.Cat;
import com.dianping.cat.CatPropertyProvider;
import com.lkx.jvm.sandbox.core.classloader.ManagerClassLoader;
import com.lkx.jvm.sandbox.core.util.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ServiceLoader;

public class LogIdAdviceListenerTest {

    @Test
    public void testAfter() {
        String currentMessageId = Cat.getCurrentMessageId();
        CatPropertyProvider next = ServiceLoader.load(CatPropertyProvider.class).iterator().next();
        System.out.println(next);
        String[] routerClass = Arrays.asList(
                "^com\\.sandbox\\.manager\\.api\\..*",
                "^com\\.alibaba\\.jvm\\.sandbox\\.api\\..*",
                "^com\\.lkx\\.jvm\\.sandbox\\..*",
                "^org\\.springframework\\..*",
                "^java\\..",
                "^com\\.dianping\\..*"
        ).toArray(new String[0]);
        ManagerClassLoader.Routing routing = new ManagerClassLoader.Routing(ManagerClassLoader.class.getClassLoader(), routerClass);

        ManagerClassLoader.Routing[] routings = {
                routing
        };
        ManagerClassLoader managerClassLoader = new ManagerClassLoader(new URL[]{builderUrl(new File("E:\\study\\sandbox\\sandbox-module\\manager-plugins\\cat-plugin-1.3.3-jar-with-dependencies.jar"))}, routings);
        CatPropertyProvider next1 = ServiceLoader.load(CatPropertyProvider.class, managerClassLoader).iterator().next();
        System.out.println(next1);
    }

    private URL builderUrl(File file) {
        try {
            File tempFile = File.createTempFile("manager_plugin", ".jar");
            tempFile.deleteOnExit();
            FileUtils.copyFile(file, tempFile);
            return new URL("file:" + tempFile.getPath());
        } catch (IOException e) {
        }
        return null;
    }
}