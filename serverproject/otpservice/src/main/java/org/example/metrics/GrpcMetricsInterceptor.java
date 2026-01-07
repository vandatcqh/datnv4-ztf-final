package org.example.metrics;

import io.grpc.*;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;

public class GrpcMetricsInterceptor implements ServerInterceptor {

    private final DoubleHistogram requestDuration;
    private final LongCounter requestCounter;
    private final LongCounter errorCounter;

    public GrpcMetricsInterceptor(Meter meter) {
        System.out.println(">>> GrpcMetricsInterceptor loaded");

        // Histogram duration in milliseconds
        this.requestDuration = meter
                .histogramBuilder("custom_grpc_server_request_duration_milliseconds")
                .setDescription("Duration of gRPC server calls in milliseconds")
                .setUnit("ms")
                .build();

        // Counter for total requests
        this.requestCounter = meter.counterBuilder("custom_grpc_server_request_total")
                .setDescription("Total number of gRPC server requests")
                .build();

        // Counter for failed requests
        this.errorCounter = meter.counterBuilder("custom_grpc_server_error_total")
                .setDescription("Total number of failed gRPC server requests")
                .build();
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        long start = System.nanoTime();
        String fullMethodName = call.getMethodDescriptor().getFullMethodName();
        String methodName = fullMethodName.substring(fullMethodName.lastIndexOf('/') + 1);

        System.out.println("📩 gRPC call intercepted: " + methodName);

        // Increment total request counter
        requestCounter.add(1, Attributes.of(AttributeKey.stringKey("endpoint"), methodName));

        ServerCall<ReqT, RespT> monitoringCall = new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void close(Status status, Metadata trailers) {
                long durationNs = System.nanoTime() - start;
                double durationMs = durationNs / 1_000_000.0; // nano → milli

                Attributes attrs = Attributes.of(
                        AttributeKey.stringKey("endpoint"), methodName,
                        AttributeKey.stringKey("status"), status.getCode().toString()
                );

                // Record duration in histogram
                requestDuration.record(durationMs, attrs);

                // Record error if status is not OK
                if (!status.isOk()) {
                    errorCounter.add(1, attrs);
                }

                System.out.printf("📊 Recorded metric for %s (%s): %.3f ms%n",
                        methodName, status.getCode(), durationMs);

                super.close(status, trailers);
            }
        };

        return next.startCall(monitoringCall, headers);
    }
}
