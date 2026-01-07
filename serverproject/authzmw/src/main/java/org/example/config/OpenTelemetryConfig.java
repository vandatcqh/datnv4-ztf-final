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
import org.example.metrics.GrpcMetricsInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OpenTelemetryConfig {

    @Bean
    public OpenTelemetrySdk openTelemetry() {
        // 👇 Đặt service.name = otp-service tại đây
        Resource resource = Resource.getDefault().merge(
                Resource.create(io.opentelemetry.api.common.Attributes.of(
                        ResourceAttributes.SERVICE_NAME, "authzmw-service"
                ))
        );

        // Metric exporter
        OtlpGrpcMetricExporter metricExporter = OtlpGrpcMetricExporter.builder()
                .setEndpoint("http://otel-collector:4317")
                .setTimeout(Duration.ofSeconds(5))
                .build();

        SdkMeterProvider meterProvider = SdkMeterProvider.builder()
                .setResource(resource)
                .registerMetricReader(
                        PeriodicMetricReader.builder(metricExporter)
                                .setInterval(Duration.ofSeconds(10))
                                .build()
                )
                .build();

        // Trace exporter (nếu có)
        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint("http://otel-collector:4317")
                .build();

        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .setResource(resource)
                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                .build();

        OpenTelemetrySdk sdk = OpenTelemetrySdk.builder()
                .setMeterProvider(meterProvider)
                .setTracerProvider(tracerProvider)
                .buildAndRegisterGlobal();

        System.out.println("✅ OpenTelemetry SDK initialized for service: authzmw-service");
        return sdk;
    }

    @Bean
    public Meter meter(OpenTelemetrySdk openTelemetry) {
        return openTelemetry.getMeter("authzmw-service");
    }

    @Bean
    public Tracer tracer(OpenTelemetrySdk openTelemetry) {
        return openTelemetry.getTracer("authzmw-service");
    }

    @Bean
    public GrpcMetricsInterceptor grpcMetricsInterceptor(Meter meter) {
        return new GrpcMetricsInterceptor(meter);
    }
}
