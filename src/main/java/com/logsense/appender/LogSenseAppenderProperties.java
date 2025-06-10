package com.logsense.appender;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "logsense.appender")
public class LogSenseAppenderProperties {

    private boolean enabled = false;
    private String endpointUrl;
    private String serviceName;
}