package org.example.dto;

import lombok.Data;

@Data
public class LoginVerify2FARequest {
    private String transaction2faId;
    private int otp;
}
