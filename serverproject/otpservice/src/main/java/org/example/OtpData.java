package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpData {
    private String otpCode;
    private String type;
    private String transactionId;
    private long createdAt;
    private long expiresAt;
    private int attemptCount;
    private boolean verified;
}