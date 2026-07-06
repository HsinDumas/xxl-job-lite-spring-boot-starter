# xxl-job-lite-spring-boot-starter

[English](README_EN.md)

[![License](https://img.shields.io/badge/license-GPLv3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0.html)
[![JDK](https://img.shields.io/badge/JDK-17+-4EB1BA.svg)](https://docs.oracle.com/en/java/javase/17/)
[![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-green.svg)](https://docs.spring.io/spring-boot/docs/current/reference/html/)
[![Author](https://img.shields.io/badge/Author-HsinDumas-orange.svg?style=flat-square)](https://github.com/HsinDumas)

适用于 xxl-job 的 Spring Boot Starter（Lite 版本，移除 EmbedServer）。

这个 starter 的目标很简单：

1. 保留 xxl-job 核心执行能力。
2. 不再额外启动 EmbedServer。
3. 直接复用你当前 Spring Boot 容器暴露 `/xxl/*` 接口。

代码基于 [XXL-JOB-CORE](https://github.com/xuxueli/xxl-job/tree/master/xxl-job-core) 二次开发，starter 版本与上游 xxl-job 版本保持一致。

## 兼容性

| 项 | latest | last verified |
| --- | --- | --- |
| starter | 见 Releases | 3.4.2 |
| xxl-job | 跟随 starter 版本 | 3.4.2 |
| Spring Boot | 3.x | 3.4.x |
| JDK (runtime) | 17+ | 17 |

最新版本请以 GitHub Releases 为准：<https://github.com/HsinDumas/xxl-job-lite-spring-boot-starter/releases>

## 快速开始

### 1) 引入依赖

```xml
<dependency>
    <groupId>com.github.hsindumas</groupId>
    <artifactId>xxl-job-lite-spring-boot-starter</artifactId>
      <version>${latest.version}</version>
</dependency>
```

请将 `${latest.version}` 替换为最新发布版本。

### 2) 配置

```yaml
xxljob:
      admin-addresses: http://127.0.0.1:8080/xxl-job-admin
      appname: demo-executor
      # 可选，不配则按当前应用地址自动推导（见下方说明）
      # address: http://127.0.0.1:8081/xxl
      # 可选
      # access-token: your-token
      # 可选，默认 3
      # log-retention-days: 3
      # 可选
      # log-path: /data/applogs/xxl-job/jobhandler
```

### 3) 在业务代码声明 Job

```java
@Component
public class DemoJob {

      @XxlJob("demoJobHandler")
      public void execute() {
            // your logic
      }
}
```

## 行为说明

1. 配置 `xxljob.appname` 后会自动注册执行器。
2. 未配置 `xxljob.address` 时，会尝试按 `http://{ip}:{port}/{contextPath}/xxl` 自动推导。
3. 如果你没有设置 `server.servlet.context-path`，建议显式配置 `xxljob.address`，避免注册地址不符合预期。
4. 在 xxl-admin 手动录入地址时，末尾请带 `/xxl`，例如 `http://demo.service/demo/xxl`。

## 配置项

| 配置项 | 必填 | 说明 |
| --- | --- | --- |
| `xxljob.admin-addresses` | 是 | xxl-admin 地址，支持逗号分隔 |
| `xxljob.appname` | 建议 | 执行器名称；有值时开启自动注册 |
| `xxljob.address` | 否 | 执行器注册地址；不填则自动推导 |
| `xxljob.access-token` | 否 | 调度鉴权 token |
| `xxljob.log-retention-days` | 否 | 日志保留天数，默认 3 |
| `xxljob.log-path` | 否 | 执行器日志目录 |

## 构建

```bash
./gradlew clean build
```

## 发布

项目使用 tag 驱动发布到 Maven Central：

```bash
git tag vX.Y.Z
git push origin vX.Y.Z
```

CI 会执行：版本校验、Central 发布、GitHub Release 生成。

## 许可证

[GPL-3.0](LICENSE)
