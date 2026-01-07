package org.example.dto;

import lombok.Data;

@Data
public class UpdateFullnameRequest {
    private int userId;
    private String fullname;
}
