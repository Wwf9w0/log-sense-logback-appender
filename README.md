This README explains what the library is, how to add it to another project, and how to configure it.

Markdown

# LogSense Logback Appender

A custom Logback appender for Spring Boot applications that sends log events as structured JSON to the LogSense Server via an HTTP endpoint.

## About The Project

This project provides a simple, auto-configurable Logback appender. Instead of writing logs to a file or the console, it serializes log events into a JSON format and transmits them to a centralized logging server, making it ideal for distributed systems and microservices architectures.

## Features

-   **Seamless Integration:** Auto-configures with Spring Boot when enabled.
-   **Structured Logging:** Sends logs as JSON, including message, level, timestamp, logger name, thread, MDC properties, and stack traces.
-   **Configurable:** Easily set the target server URL and service name through `application.properties` or `application.yml`.
-   **Conditional:** Can be enabled or disabled with a single property, causing no overhead when turned off.

## Prerequisites

-   Java 21 or later
-   Maven 3.8+

## How to Use

To use this appender in your Spring Boot application (e.g., `document-service`), follow these steps.

### 1. Install the Appender Locally

First, build this project and install it into your local Maven repository (`.m2` folder).

Navigate to the `log-sense-logback-appender` project's root directory and run:

```bash
mvn clean install
2. Add as a Dependency
In your Spring Boot application's pom.xml, add the following dependency:

XML

<dependency>
    <groupId>com.log-appender</groupId>
    <artifactId>log-sense-logback-appender</artifactId>
    <version>1.0.0</version>
</dependency>
3. Configure the Appender
In your application.properties or application.yml file, provide the necessary configuration to enable and point the appender to your logsense-server.

Using application.properties:

Properties

# REQUIRED: Must be true to enable the appender
logsense.appender.enabled=true

# REQUIRED: The full URL of the logsense-server endpoint
logsense.appender.endpoint-url=http://localhost:8080/api/logs

# REQUIRED: The name of the service sending the logs
logsense.appender.service-name=document-service
Using application.yml:

YAML

logsense:
  appender:
    # REQUIRED: Must be true to enable the appender
    enabled: true
    # REQUIRED: The full URL of the logsense-server endpoint
    endpoint-url: http://localhost:8080/api/logs
    # REQUIRED: The name of the service sending the logs
    service-name: document-service
4. Start Logging!
That's it! Now, any log event generated in your application using SLF4J (log.info(...), log.error(...), etc.) will be automatically sent to the LogSense Server.

Building from Source
If you want to contribute or modify the appender itself, clone the repository and build it using Maven.

Bash

git clone <your-repo-url>
cd log-sense-logback-appender
mvn clean install
License
Distributed under the MIT License. See LICENSE for more information.
