package org.example.metrics;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import org.example.dto.ApiStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class HttpMetricsFilter implements Filter {

    private final DoubleHistogram requestDuration;
    private final LongCounter requestCounter;
    private final LongCounter errorCounter;

    public HttpMetricsFilter(Meter meter) {
        // Histogram duration in milliseconds
        this.requestDuration = meter
                .histogramBuilder("custom_http_server_request_duration_milliseconds")
                .setDescription("Duration of HTTP server requests (custom) in milliseconds")
                .setUnit("ms")
                .build();

        this.requestCounter = meter.counterBuilder("custom_http_server_request_total")
                .setDescription("Total number of HTTP server requests (custom)")
                .build();

        this.errorCounter = meter.counterBuilder("custom_http_server_error_total")
                .setDescription("Total number of failed HTTP server requests (custom)")
                .build();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        long start = System.nanoTime();
        try {
            chain.doFilter(req, res);
        } finally {
            long durationNs = System.nanoTime() - start;
            double durationMs = durationNs / 1_000_000.0; // nano → milli

            String endpoint = request.getRequestURI();
            String method = request.getMethod();
            String status = request.getAttribute("apiStatus") != null
                    ? ((ApiStatus) request.getAttribute("apiStatus")).name()
                    : String.valueOf(response.getStatus());

            Attributes attrs = Attributes.of(
                    AttributeKey.stringKey("endpoint"), endpoint,
                    AttributeKey.stringKey("method"), method,
                    AttributeKey.stringKey("status"), status
            );

            // Record histogram
            requestDuration.record(durationMs, attrs);

            // Increment counters
            requestCounter.add(1, attrs);

            if (response.getStatus() >= 400) {
                errorCounter.add(1, attrs);
            }

            // Optional: log to console
            System.out.printf("📊 HTTP %s %s [%s]: %.3f ms%n",
                    method, endpoint, status, durationMs);
        }
    }
}
