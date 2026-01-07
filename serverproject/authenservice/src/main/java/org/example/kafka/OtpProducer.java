package org.example.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final String topic = "otp-topic";

    public void sendOtp(String email, String otp) {
        String message = email + "|" + otp;
        kafkaTemplate.send(topic, email, message);
        System.out.println("Sent OTP message to Kafka: " + message);
    }
}
