package org.example.dto;

import lombok.Data;

@Data
public class ResendOtpRequest {
    private String email;
    private String transactionId; // transaction cũ


}
