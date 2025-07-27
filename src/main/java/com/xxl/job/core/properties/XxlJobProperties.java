package com.xxl.job.core.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * @author Dumas
 */
@Validated
@ConfigurationProperties(prefix = XxlJobProperties.PREFIX)
public class XxlJobProperties {
    public static final String PREFIX = "xxljob";
    /**
     * logRetentionDays
     */
    private final Integer logRetentionDays = 3;
    /**
     * adminAddresses
     */
    @NotBlank
    private String adminAddresses;
    /**
     * accessToken
     */
    private String accessToken;
    /**
     * If it is empty, turn off auto-register
     */
    private String appname;
    /**
     * If the appname is not empty, it is automatically assembled with the IP and port of the current container,
     * it can be overwritten if necessary
     */
    private String address;
    /**
     * default is /data/applogs/xxl-job/jobhandler
     */
    private String logPath;

    public Integer getLogRetentionDays() {
        return logRetentionDays;
    }

    public String getAdminAddresses() {
        return adminAddresses;
    }

    public void setAdminAddresses(String adminAddresses) {
        this.adminAddresses = adminAddresses;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }
}
