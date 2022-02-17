

# manager的部署管理

## 下载源码

1. 下载该源码
2. 编译源码， 通过maven进行install

由于manager是作为sandbox的一个模块，所以需要拥有一套sandbox的沙箱环境：[sandbox环境搭建](./JVM-SANDBOX-USER-GUIDE-Chinese.md)

这个时候你可以将源码中编译完成的`sandbox-manager-module-1.3.3-jar-with-dependencies.jar`包放入

![image-20220210102654026](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/image-20220210102654026.png)

sandbox-module的文件夹中，沙箱环境在启动的时候会回调这个模块的功能。

## 另外一些需要注意的地方



1. 你安装的sandbox的目录地址，在`JVM-Sandbox\pom.xml` 中找到`sandbox.path` 进行配置。后续编译的包都会根据该路径复制到对应的文件夹目录中，方便调试

   

## 启动应用以及调试

[jvm-sandbox实战之windows调试](https://blog.csdn.net/lkx444368875/article/details/121330657)

[jvm-sandbox实战之编写简单案例](https://blog.csdn.net/lkx444368875/article/details/121202886)

[基于jvm-sandbox IDEA Debug 调试(偏远程调试)](https://blog.csdn.net/lkx444368875/article/details/121244253)

[实战之jvm-sandbox动态加载插件实现](https://blog.csdn.net/lkx444368875/article/details/121809290)