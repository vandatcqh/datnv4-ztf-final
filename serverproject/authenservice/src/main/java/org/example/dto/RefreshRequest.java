package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshRequest {
    @NotBlank
    private int userId;
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
