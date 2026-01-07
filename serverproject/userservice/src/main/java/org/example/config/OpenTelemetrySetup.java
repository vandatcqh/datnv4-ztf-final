package org.example.config;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.ResourceAttributes;

import java.time.Duration;

public class OpenTelemetrySetup {

    private static Meter meter;
    private static Tracer tracer;

    public static void init(String otlpEndpoint) {
        if (meter != null && tracer != null) {
            return; // đã init rồi, bỏ qua
        }
        Resource resource = Resource.getDefault().toBuilder()
                .put(ResourceAttributes.SERVICE_NAME, "user-service")
                .build();

        // Metrics exporter
        OtlpGrpcMetricExporter metricExporter = OtlpGrpcMetricExporter.builder()
                .setEndpoint(otlpEndpoint)
                .build();

        SdkMeterProvider meterProvider = SdkMeterProvider.builder()
                .setResource(resource)
                .registerMetricReader(
                        PeriodicMetricReader.builder(metricExporter)
                                .setInterval(Duration.ofSeconds(10))
                                .build()
                )
                .build();

        // Trace exporter
        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint(otlpEndpoint)
                .build();

        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .setResource(resource)
                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                .build();

        // Combine both
        OpenTelemetrySdk openTelemetry = OpenTelemetrySdk.builder()
                .setMeterProvider(meterProvider)
                .setTracerProvider(tracerProvider)
                .buildAndRegisterGlobal();

        meter = GlobalOpenTelemetry.getMeter("user.service");
        tracer = GlobalOpenTelemetry.getTracer("user.service");
    }

    public static Meter getMeter() {
        if (meter == null) {
            throw new IllegalStateException("OpenTelemetry not initialized. Call init() first.");
        }
        return meter;
    }

    public static Tracer getTracer() {
        if (tracer == null) {
            throw new IllegalStateException("OpenTelemetry not initialized. Call init() first.");
        }
        return tracer;
    }
}
