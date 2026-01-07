package org.example.config;

import io.opentelemetry.api.GlobalOpenTelemetry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class OpenTelemetryInitCheck implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("🔍 OpenTelemetry global instance: " + GlobalOpenTelemetry.get());
    }
}
