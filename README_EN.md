# xxl-job-lite-spring-boot-starter

[中文](README.md)

[![License](https://img.shields.io/badge/license-GPLv3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0.html)
[![JDK](https://img.shields.io/badge/JDK-17+-4EB1BA.svg)](https://docs.oracle.com/en/java/javase/17/)
[![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-green.svg)](https://docs.spring.io/spring-boot/docs/current/reference/html/)
[![Author](https://img.shields.io/badge/Author-HsinDumas-orange.svg?style=flat-square)](https://github.com/HsinDumas)

A Spring Boot starter for xxl-job (Lite edition, EmbedServer removed).

This starter is intentionally simple:

1. Keep the core xxl-job executor behavior.
2. Do not start an extra EmbedServer.
3. Reuse your existing Spring Boot container to expose `/xxl/*` endpoints.

The code is based on [XXL-JOB-CORE](https://github.com/xuxueli/xxl-job/tree/master/xxl-job-core), and the starter version stays aligned with upstream xxl-job.

## Compatibility

| Item | latest | last verified |
| --- | --- | --- |
| starter | see Releases | 3.4.2 |
| xxl-job | follows starter version | 3.4.2 |
| Spring Boot | 3.x | 3.4.x |
| JDK (runtime) | 17+ | 17 |

For the latest version, see GitHub Releases: <https://github.com/HsinDumas/xxl-job-lite-spring-boot-starter/releases>

## Quick Start

### 1) Add dependency

```xml
<dependency>
    <groupId>com.github.hsindumas</groupId>
    <artifactId>xxl-job-lite-spring-boot-starter</artifactId>
    <version>${latest.version}</version>
</dependency>
```

Replace `${latest.version}` with the latest published version.

### 2) Configure

```yaml
xxljob:
  admin-addresses: http://127.0.0.1:8080/xxl-job-admin
  appname: demo-executor
  # Optional. If omitted, address is derived automatically (see notes below)
  # address: http://127.0.0.1:8081/xxl
  # Optional
  # access-token: your-token
  # Optional, default is 3
  # log-retention-days: 3
  # Optional
  # log-path: /data/applogs/xxl-job/jobhandler
```

### 3) Declare jobs in business code

```java
@Component
public class DemoJob {

    @XxlJob("demoJobHandler")
    public void execute() {
        // your logic
    }
}
```

## Runtime Behavior

1. If `xxljob.appname` is configured, the executor auto-registers.
2. If `xxljob.address` is not configured, the default address is derived as `http://{ip}:{port}/{contextPath}/xxl`.
3. If you do not set `server.servlet.context-path`, explicitly set `xxljob.address` to avoid unexpected registration addresses.
4. If you manually register in xxl-admin, ensure the address ends with `/xxl`, for example `http://demo.service/demo/xxl`.

## Configuration Keys

| Key | Required | Description |
| --- | --- | --- |
| `xxljob.admin-addresses` | Yes | xxl-admin endpoint, comma-separated is supported |
| `xxljob.appname` | Recommended | Executor app name; enables auto-registration when present |
| `xxljob.address` | No | Executor registry address; auto-derived when absent |
| `xxljob.access-token` | No | Scheduling auth token |
| `xxljob.log-retention-days` | No | Log retention days, default is 3 |
| `xxljob.log-path` | No | Executor log directory |

## Build

```bash
./gradlew clean build
```

## Release

Tag-driven Maven Central release:

```bash
git tag vX.Y.Z
git push origin vX.Y.Z
```

CI will run version checks, publish to Central, and create GitHub Release notes.

## License

[GPL-3.0](LICENSE)
