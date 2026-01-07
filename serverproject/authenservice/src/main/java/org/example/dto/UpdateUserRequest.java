package org.example.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private int userId;
    private String fullname;
    private String email;
    private String password; // optional
}
