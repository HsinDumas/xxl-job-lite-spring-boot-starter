package com.xxl.job.core;

import com.xxl.job.core.controller.XxlController;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.properties.XxlJobProperties;
import com.xxl.tool.http.IPTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

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
            xxlJobSpringExecutor.setAppname(xxlJobProperties.getAppname());

            if (StringUtils.hasLength(xxlJobProperties.getAddress())) {
                xxlJobSpringExecutor.setAddress(xxlJobProperties.getAddress());
            } else {
                String address = "http://" + IPTool.toAddressString(IPTool.getIp(), serverProperties.getPort());
                String contextPath = serverProperties.getServlet() == null
                        ? null
                        : serverProperties.getServlet().getContextPath();
                if (StringUtils.hasLength(contextPath)) {
                    if (contextPath.startsWith("/")) {
                        address = address + contextPath;
                    } else {
                        address = address + "/" + contextPath;
                    }
                }
                xxlJobSpringExecutor.setAddress(address + "/xxl");
            }
        }

        log.info(">>>>>>>>>>> [完成] xxl-job config init. Executor = {}", xxlJobSpringExecutor);
        return xxlJobSpringExecutor;
    }
}
