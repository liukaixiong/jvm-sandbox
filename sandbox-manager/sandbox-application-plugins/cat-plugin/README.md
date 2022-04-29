# Cat 无侵入埋点文档

该插件是以CAT为基础，为一些业务功能进行埋点拦截。

目前支持的功能:
- SpringMvc
- servlet
- sl4j
- mybatis
- dubbo

## 配置相关

基于Mybatis相关的配置

```yaml
# SQL拦截相关的,该配置是基于mybatis-plus的
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.slf4j.Slf4jImpl
# 当然Mybatis原生的配置也差不多
mybatis.configuration.log-impl=org.apache.ibatis.logging.slf4j.Slf4jImpl
# 打印SQL日誌
logging.level.${你的包根路径}=DEBUG
```

基于Service的配置,这里稍微比较纠结的原因是:
1. 如果客户端不指定的话,拦截的范围不太好确定.比如我只想拦截service层的

```yaml
spring:
  sandbox:
    plugin:
      register:
        cat:
          enhanceConfigs:
            - classPattern: com.sandbox.demo.service.*
              methodNames: '*'
              isEqualsInterface: true
              adviceNames:
                - CAT_METHOD_TRANSACTION_POINT
```

