# sandbox 管理器插件

负责管理每个被agent应用的服务，通过心跳的方式注册到web平台，由web平台统一管理每个应用的功能和同级。

该管理器集成了Spring相关的功能，让开发者能够更加容易上手。

### 模块的划分

**sandbox-manager**

- sandbox-application-plugins : 插件的开发

- sandbox-manager-api : 一些通用的接口规则定义

- sandbox-manager-core : 核心工具的一些实现

- sandbox-manager-module : sandbox插件的module定义，agent的核心入口

  > 实现了sandbox的module管理，这是manager的核心

- sandbox-manager-web: UI管理界面，作为manager的在线管理

## 流程介绍

#### **manager的工作流程**

![image-20220210101608750](https://gitee.com/liukaixiong/drawing-bed/raw/master/image/image-20220210101608750.png)

## 环境的部署以及开发建议

[详情参考](../doc/sandbox-manager-deploy.md)

## 插件开发

目前的插件粒度划分是以功能模块进行划分的，目前已经有部分开发完成的插件可以提供参考。

[插件文档](./sandbox-application-plugins/README.md)

