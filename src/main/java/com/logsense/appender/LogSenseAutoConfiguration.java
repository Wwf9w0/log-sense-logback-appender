package com.logsense.appender;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LogSenseAppenderProperties.class)
@ConditionalOnProperty(prefix = "logsense.appender", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class LogSenseAutoConfiguration {

    private final LogSenseAppenderProperties properties;

    @PostConstruct
    public void initialize() {
        System.out.println("LogSense Auto-Configuration is enabled. Initializing LogSenseAppender...");

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        LogSenseAppender logSenseAppender = new LogSenseAppender();
        logSenseAppender.setContext(context);

        logSenseAppender.setEndpointUrl(properties.getEndpointUrl());
        logSenseAppender.setServiceName(properties.getServiceName());

        logSenseAppender.start();

        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(logSenseAppender);

        System.out.println("LogSenseAppender attached to ROOT logger.");
    }
}
