package org.example.config;

import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTelemetryConfig {

    @Value("${otel.exporter.otlp.endpoint}")
    private String otlpEndpoint;

    @Bean
    public Meter meter() {
        OpenTelemetrySetup.init(otlpEndpoint); // init OTLP SDK
        return OpenTelemetrySetup.getMeter();   // trả Meter cho Spring
    }
    @Bean
    public Tracer tracer() {
        OpenTelemetrySetup.init(otlpEndpoint);
        return OpenTelemetrySetup.getTracer();
    }
}
