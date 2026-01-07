// DTO cho 2FA
package org.example.dto;

import lombok.Data;

@Data
public class TwoFARequest {
    private Integer userId;
    private Integer otp; // chỉ dùng cho verify, setup thì có thể bỏ
}
