package org.example.metrics;

import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import org.springframework.stereotype.Component;

@Component
public class RegisterMetrics {

    private final LongHistogram bcryptHistogram;
    private final LongHistogram otpHistogram;
    private final LongHistogram redisHistogram;
    private final LongHistogram kafkaHistogram;
    private final LongHistogram dbHistogram; // thêm DB

    public RegisterMetrics(Meter meter) {
        this.bcryptHistogram = meter.histogramBuilder("register_bcrypt_latency_ms")
                .setDescription("Time spent on bcrypt password hashing")
                .ofLongs()
                .build();

        this.otpHistogram = meter.histogramBuilder("register_otp_latency_ms")
                .setDescription("Time spent on OTP generation")
                .ofLongs()
                .build();

        this.redisHistogram = meter.histogramBuilder("register_redis_latency_ms")
                .setDescription("Time spent on Redis operations")
                .ofLongs()
                .build();

        this.kafkaHistogram = meter.histogramBuilder("register_kafka_latency_ms")
                .setDescription("Time spent on sending Kafka OTP")
                .ofLongs()
                .build();

        this.dbHistogram = meter.histogramBuilder("register_db_latency_ms") // DB histogram
                .setDescription("Time spent on saving user to database")
                .ofLongs()
                .build();
    }

    public void recordBcrypt(long durationMs) {
        bcryptHistogram.record(durationMs);
    }

    public void recordOtp(long durationMs) {
        otpHistogram.record(durationMs);
    }

    public void recordRedis(long durationMs) {
        redisHistogram.record(durationMs);
    }

    public void recordKafka(long durationMs) {
        kafkaHistogram.record(durationMs);
    }

    public void recordDb(long durationMs) { // method mới
        dbHistogram.record(durationMs);
    }
}
