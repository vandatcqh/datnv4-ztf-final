package org.example.metrics;

import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatMetric {

    private final LongCounter heartbeatCounter;

    public HeartbeatMetric(Meter meter) {
        this.heartbeatCounter = meter.counterBuilder("service_heartbeat")
                .setDescription("Heartbeat metric for health check")
                .build();
    }

    @Scheduled(fixedRate = 5000) // 10s
    public void sendHeartbeat() {
        heartbeatCounter.add(1);
    }
}
