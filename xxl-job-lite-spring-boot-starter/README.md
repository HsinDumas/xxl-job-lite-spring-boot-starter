# xxl-job-lite-spring-boot-starter

[![License](https://img.shields.io/badge/license-GPLv3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0.html)
[![License](https://img.shields.io/badge/JDK-17+-4EB1BA.svg)](https://docs.oracle.com/en/java/javase/17/)
[![License](https://img.shields.io/badge/SpringBoot-3+-green.svg)](https://docs.spring.io/spring-boot/docs/2.1.5.RELEASE/reference/htmlsingle/)
[![Author](https://img.shields.io/badge/Author-HsinDumas-orange.svg?style=flat-square)](https://github.com/HsinDumas)

**适用于 xxl-job 的 spring-boot-starter (阉割 embedServer)**。

1. 目前只适配了 Spring Boot 3.x
2. 从 xxl-job 3.1.1 开始，同步更新 starter
   版本，代码基于 [XXL-JOB-CORE](https://github.com/xuxueli/xxl-job/tree/master/xxl-job-core) 二次开发，版本号保持一致
3. [XXL-JOB-CORE](https://github.com/xuxueli/xxl-job/tree/master/xxl-job-core) 源码中，通过 EmbedServer
   创建新的容器与端口来暴露相关接口给 xxl-admin调度，这样导致对容器的管理和资源的监控都是缺失的，本插件直接将相关接口(
   /xxl/...)申明在当前容器中，移除启动 EmbedServer的相关代码，其余保持不变

> 🚀项目持续优化迭代，欢迎大家提ISSUE和PR！麻烦大家能给一颗star✨，您的star是我们持续更新的动力！

## 快速开始

### 引入依赖

```xml

<dependency>
    <groupId>com.github.hsindumas</groupId>
    <artifactId>xxl-job-lite-spring-boot-starter</artifactId>
    <version>3.1.1</version>
</dependency>
```

### 配置属性

```yaml
xxljob:
  admin-addresses: ...
  appname: ...
  address: ...
  access-token: ...
```

1. 对 `XxlJobSpringExecutor` 的配置项进行精简。
2. 配置 appname 后将自动注册执行器，请按需配置。
3. 当 appname 配置后，将开启自动注册，address 默认会以 `http://{ip:port}/{contextPath}/xxl` 的形式注册，如果需要自定义地址，可以自行配置。
4. 手动录入时，机器地址的最后请务必加上`/{contextPath}/xxl`,如 `http://demo.service/demo/xxl`
