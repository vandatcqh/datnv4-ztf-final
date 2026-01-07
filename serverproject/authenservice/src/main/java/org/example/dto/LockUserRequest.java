package org.example.dto;

import lombok.Data;

@Data
public class LockUserRequest {
    private int userId;
    private long availableFrom;
}
