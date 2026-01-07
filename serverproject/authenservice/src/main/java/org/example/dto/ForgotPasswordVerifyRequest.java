package org.example.dto;

import lombok.Data;

@Data
public class ForgotPasswordVerifyRequest {
    private String transactionId;
    private String otp;
    private String newPassword;
}