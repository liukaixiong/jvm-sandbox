package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.jvm.sandbox.module.manager.classloader.MyClassLoader;
import com.lkx.jvm.sandbox.core.classloader.ManagerClassLoader;
import com.lkx.jvm.sandbox.core.util.FileUtils;
import com.sandbox.manager.api.Trace;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/12/27 - 17:27
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {
        File file = new File("E:\\study\\sandbox\\sandbox-module\\manager-plugins\\cat-plugin-1.3.3-jar-with-dependencies.jar");
//        URL urls = new URL("file:C:/Users/liukx/AppData/Local/Temp/manager_plugin124980413499729388.jar");
        Map<String, AnnotationConfigApplicationContext> cacheMap = new HashMap<>();


        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("请输入执行 [1 : 加载 , 3 : 卸载]");
            int next = input.nextInt();
            System.out.println("接收到的指令:" + next);

            if (1 == next) {
                clearClassLoader(cacheMap);
                AnnotationConfigApplicationContext applicationContext = newManager(file);
                cacheMap.put("A", applicationContext);
            } else if (2 == next) {
                clearClassLoader(cacheMap);
            } else if (3 == next) {
                System.gc();
                System.out.println("触发了一次GC操作!");
            }
        }
    }

    private static void clearClassLoader(Map<String, AnnotationConfigApplicationContext> cacheMap) throws IOException {
        AnnotationConfigApplicationContext context = cacheMap.remove("A");
        Optional.ofNullable(context).ifPresent((c) -> {
            ManagerClassLoader classLoader = (ManagerClassLoader) c.getClassLoader();
            try {
                Objects.requireNonNull(classLoader).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("清除缓存");
        });
    }

    private static AnnotationConfigApplicationContext newManager(File file) {
        List<String> includeClass = new ArrayList<>();
        includeClass.add("^com\\.sandbox\\.manager\\.api\\..*");
        includeClass.add("^com\\.alibaba\\.jvm\\.sandbox\\.api\\..*");
//        includeClass.add("^com\\.lkx\\..*"); // 原因在此
//        // includeClass.add("^org\\.apache\\.commons\\.lang3\\..*");
        includeClass.add("^org\\.springframework\\..*");
//        includeClass.add("^java\\..*");

        ManagerClassLoader urlClassLoader = new ManagerClassLoader(new URL[]{builderUrl(file)}, new ManagerClassLoader.Routing(
                ClassLoaderTest.class.getClassLoader(),
                includeClass.toArray(includeClass.toArray(new String[0]))));
        AnnotationConfigApplicationContext pluginApplicationContext = new AnnotationConfigApplicationContext();
        pluginApplicationContext.setClassLoader(urlClassLoader);
        pluginApplicationContext.scan("com.sandbox.application.plugin");
        pluginApplicationContext.refresh();

        Trace bean = pluginApplicationContext.getBean(Trace.class);
        String id = bean.getId();
        System.out.println(">>>>> 执行 :: " + id);
        return pluginApplicationContext;
    }

    private static AnnotationConfigApplicationContext newMyClassLoader(File file) {
        MyClassLoader urlClassLoader = new MyClassLoader(new URL[]{builderUrl(file)});
        AnnotationConfigApplicationContext pluginApplicationContext = new AnnotationConfigApplicationContext();
        pluginApplicationContext.setClassLoader(urlClassLoader);
        pluginApplicationContext.scan("com.sandbox.application.plugin");
        pluginApplicationContext.refresh();
        return pluginApplicationContext;
    }

    private static AnnotationConfigApplicationContext newApplication(File file) {
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{builderUrl(file)}, ClassLoaderTest.class.getClassLoader());
        AnnotationConfigApplicationContext pluginApplicationContext = new AnnotationConfigApplicationContext();
        pluginApplicationContext.setClassLoader(urlClassLoader);
        pluginApplicationContext.scan("com.sandbox.application.plugin");
        pluginApplicationContext.refresh();
        return pluginApplicationContext;
    }

    private static URL builderUrl(File file) {
        try {
            File tempFile = File.createTempFile("manager_plugin", ".jar");
            tempFile.deleteOnExit();
            FileUtils.copyFile(file, tempFile);
            return new URL("file:" + tempFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
