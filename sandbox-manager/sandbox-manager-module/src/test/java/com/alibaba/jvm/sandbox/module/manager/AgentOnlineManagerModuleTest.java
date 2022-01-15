package com.alibaba.jvm.sandbox.module.manager;

import com.alibaba.jvm.sandbox.module.manager.plugin.NullPluginProcessorLifeCycle;
import com.alibaba.jvm.sandbox.module.manager.plugin.PluginManager;
import com.lkx.jvm.sandbox.core.factory.GlobalFactoryHelper;
import com.sandbox.manager.api.MethodAdviceInvoke;
import com.sandbox.manager.api.Trace;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

public class AgentOnlineManagerModuleTest {
    private final AgentOnlineManagerModule module = new AgentOnlineManagerModule();

    @Test
    public void testRefreshManagerFactory() throws Exception {
        module.refreshManagerFactory();
        AnnotationConfigApplicationContext annotationConfigApplicationContext = module.getAnnotationConfigApplicationContext();
        PluginManager object = GlobalFactoryHelper.getInstance().getObject(PluginManager.class);
        GlobalFactoryHelper.plugin().getObject(Trace.class).getId();
        GlobalFactoryHelper.plugin().getList(MethodAdviceInvoke.class);
        System.out.println(annotationConfigApplicationContext);
    }


    @Test
    public void testRefresh() throws Exception {
        // 刷新主容器插件
        module.refreshManagerFactory();
        AnnotationConfigApplicationContext annotationConfigApplicationContext = module.getAnnotationConfigApplicationContext();
        PluginManager refresh = annotationConfigApplicationContext.getBean(PluginManager.class);
        String pluginPath = "E:\\study\\sandbox\\sandbox-module\\manager-plugins";
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("请输入执行 [1 : 加载 , 2 : 卸载]");
            int next = input.nextInt();
            System.out.println("接收到的指令:" + next);

            if (1 == next) {
                refresh.loadPlugin(pluginPath, null, null);
            } else if (2 == next) {
                refresh.unloadInstance("cat-plugin-1.3.3-jar-with-dependencies.jar");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        AgentOnlineManagerModule module = new AgentOnlineManagerModule();
        module.refreshManagerFactory();
        AnnotationConfigApplicationContext annotationConfigApplicationContext = module.getAnnotationConfigApplicationContext();
        PluginManager refresh = annotationConfigApplicationContext.getBean(PluginManager.class);

        String pluginPath = "E:\\study\\sandbox\\sandbox-module\\manager-plugins";
//
//        int count = 10000000;
//        for (int i = 0; i < count; i++) {
//            if (Math.floorMod(i, 1000) == 0) {
//                System.out.println(" 当前执行 : " + i);
//            }
//            refresh.loadPlugin(pluginPath);
//        }
//
//
//        System.out.println(Strings.lenientFormat(" 执行 %s 次 ", count));

        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("请输入执行 [1 : 加载 , 2 : 卸载]");
            int next = input.nextInt();
            System.out.println("接收到的指令:" + next);

            if (1 == next) {
                refresh.loadPlugin(pluginPath, null, new NullPluginProcessorLifeCycle());
            } else if (2 == next) {
                refresh.unloadInstance("cat-plugin-1.3.3-jar-with-dependencies.jar");
            } else if (3 == next) {
                System.gc();
                System.out.println("触发了一次GC操作!");
            }
        }
//        ManagerClassLoader managerClassLoader = new ManagerClassLoader(new URL[]{builderUrl(jarFile)}, new ManagerClassLoader.Routing(
//                AgentOnlineManagerModuleTest.class.getClassLoader(),
//                "^com\\.sandbox\\.manager\\.api\\..*", "^com\\.alibaba\\.jvm\\.sandbox\\.api\\..*"
//        ));
    }
}