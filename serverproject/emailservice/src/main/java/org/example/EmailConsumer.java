package org.example;

import org.example.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "otp-topic", groupId = "email-service-group")
    public void consume(String message) {
        System.out.println("Received message from Kafka: " + message);

        // Tách chuỗi "email|otp"
        String[] parts = message.split("\\|");
        if (parts.length != 2) {
            System.err.println("Invalid message format: " + message);
            return;
        }

        String to = parts[0];
        String otp = parts[1];

        // Tạo nội dung email
        String emailBody = "Xin chào, đây là mã OTP của bạn: " + otp;

        // Gửi email
        //emailService.sendMail(to, "Mã OTP của bạn", emailBody);
    }
}
