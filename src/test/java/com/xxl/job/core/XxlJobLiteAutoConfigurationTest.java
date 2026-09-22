package com.xxl.job.core;

import com.xxl.job.core.controller.XxlController;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke test for the lite starter: the auto-configuration must boot on Spring Boot 4 and
 * derive the executor registry address the same way the README documents it.
 *
 * <p>The registry thread only dials the admin when {@code xxljob.appname} is set, so cases
 * without an appname stay fully offline.
 */
class XxlJobLiteAutoConfigurationTest {

    @TempDir
    Path logDir;

    private ApplicationContextRunner runner() {
        return new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(XxlJobLiteAutoConfiguration.class))
                .withPropertyValues(
                        "xxljob.admin-addresses=http://127.0.0.1:65500/xxl-job-admin",
                        "xxljob.log-path=" + logDir);
    }

    @Test
    void autoConfigurationRegistersExecutorAndController() {
        runner().run(context -> assertThat(context)
                .hasSingleBean(XxlJobSpringExecutor.class)
                .hasSingleBean(XxlController.class));
    }

    @Test
    void noAppnameMeansNoRegistration() {
        runner().run(context -> {
            XxlJobSpringExecutor executor = context.getBean(XxlJobSpringExecutor.class);
            assertThat(executor.getAppname()).isNull();
            assertThat(executor.getAddress()).isNull();
        });
    }

    @Test
    void explicitAddressWinsOverDerivation() {
        runner().withPropertyValues(
                        "xxljob.appname=demo-executor",
                        "xxljob.address=http://example.test/custom/xxl",
                        "server.port=8081")
                .run(context -> assertThat(context.getBean(XxlJobSpringExecutor.class).getAddress())
                        .isEqualTo("http://example.test/custom/xxl"));
    }

    @Test
    void addressIsDerivedFromServerPortAndContextPath() {
        runner().withPropertyValues(
                        "xxljob.appname=demo-executor",
                        "server.port=8081",
                        "server.servlet.context-path=/demo")
                .run(context -> assertThat(context.getBean(XxlJobSpringExecutor.class).getAddress())
                        .startsWith("http://")
                        .endsWith(":8081/demo/xxl"));
    }

    @Test
    void addressFallsBackToRootWhenNoContextPath() {
        runner().withPropertyValues(
                        "xxljob.appname=demo-executor",
                        "server.port=8081")
                .run(context -> assertThat(context.getBean(XxlJobSpringExecutor.class).getAddress())
                        .endsWith(":8081/xxl"));
    }
}
