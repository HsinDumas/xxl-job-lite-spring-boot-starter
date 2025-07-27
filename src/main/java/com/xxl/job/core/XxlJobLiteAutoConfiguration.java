package com.xxl.job.core;

import com.xxl.job.core.controller.XxlController;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.properties.XxlJobProperties;
import com.xxl.job.core.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * @author Dumas
 */
@AutoConfiguration
@EnableConfigurationProperties({XxlJobProperties.class, ServerProperties.class})
public class XxlJobLiteAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(XxlJobLiteAutoConfiguration.class);

    private final XxlJobProperties xxlJobProperties;
    private final ServerProperties serverProperties;

    public XxlJobLiteAutoConfiguration(XxlJobProperties xxlJobProperties, ServerProperties serverProperties) {
        this.xxlJobProperties = xxlJobProperties;
        this.serverProperties = serverProperties;
    }

    @Bean
    public XxlController xxlController() {
        return new XxlController();
    }

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> [开始] xxl-job config init.");

        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdminAddresses());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());
        if (StringUtils.hasLength(xxlJobProperties.getAppname())) {
            // auto register
            if (StringUtils.hasLength(xxlJobProperties.getAddress())) {
                xxlJobSpringExecutor.setAddress(xxlJobProperties.getAddress());
            } else {
                xxlJobSpringExecutor.setAddress("http://{ip_port}/{contextPath}/xxl"
                        .replace("{ip_port}", IpUtil.getIpPort(IpUtil.getIp(), serverProperties.getPort()))
                        .replace("{contextPath}", serverProperties.getServlet().getContextPath()));
            }
        }
        log.info(">>>>>>>>>>> [完成] xxl-job config init. Executor = {}", xxlJobSpringExecutor);
        return xxlJobSpringExecutor;
    }
}
