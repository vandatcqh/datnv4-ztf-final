package org.example.metrics;

import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.stereotype.Component;

@Component
@GrpcGlobalServerInterceptor
public class GlobalGrpcMetricsInterceptor extends GrpcMetricsInterceptor implements ServerInterceptor {
    public GlobalGrpcMetricsInterceptor(io.opentelemetry.api.metrics.Meter meter) {
        super(meter);
    }
}
