package com.logsense.appender;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.AppenderBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor
public class LogSenseAppender extends AppenderBase<ILoggingEvent> {
    private String endpointUrl;
    private String serviceName;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        if (endpointUrl == null || serviceName == null) {
            addError("Endpoint URL or Service Name is not configured for LogSenseAppender.");
            return;
        }

        try {
            LogEventDTO dto = createDto(iLoggingEvent);
            String jsonPayload = objectMapper.writeValueAsString(dto);
            sendRequest(jsonPayload);

        } catch (Exception e) {
            addError("Failed to send log to LogSense server", e);
        }
    }

    private LogEventDTO createDto(ILoggingEvent event) {
        LogEventDTO dto = new LogEventDTO();
        dto.setMessage(event.getFormattedMessage());
        dto.setServiceName(this.serviceName);
        dto.setLogLevel(event.getLevel().toString());
        dto.setLoggerName(event.getLoggerName());
        dto.setThreadName(event.getThreadName());
        dto.setTimestamp(event.getTimeStamp());
        dto.setMdc(event.getMDCPropertyMap());

        IThrowableProxy throwableProxy = event.getThrowableProxy();
        if (throwableProxy != null) {
            dto.setStackTrace(ThrowableProxyUtil.asString(throwableProxy));
        }
        return dto;
    }

    private void sendRequest(String jsonPayload) {

        try {
            URL url = new URL(this.endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_ACCEPTED && responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("LogSneseAppender failed to send log, response code: " + responseCode);
            }
            connection.disconnect();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}