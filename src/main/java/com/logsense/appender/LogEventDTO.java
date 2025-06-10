package com.logsense.appender;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class LogEventDTO implements Serializable {

    private String message;
    private String serviceName;
    private String logLevel;
    private String stackTrace;
    private String threadName;
    private long timestamp;
    private String loggerName;
    private Map<String, String> mdc;
}
