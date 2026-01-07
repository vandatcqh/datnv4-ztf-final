package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ApiResponse {
    private int status;
    private String message;
    private Map<String, Object> data; // thêm trường data

    public static ApiResponse of(ApiStatus status, String message) {
        return ApiResponse.builder()
                .status(status.getCode())
                .message(message != null ? message : status.getDefaultMessage())
                .build();
    }

    public static ApiResponse of(ApiStatus status, String message, Map<String, Object> data) {
        return ApiResponse.builder()
                .status(status.getCode())
                .message(message != null ? message : status.getDefaultMessage())
                .data(data)
                .build();
    }
//    @JsonIgnore
//    public String getStatusEnum() {
//        ApiStatus s = ApiStatus.fromCode(this.status);
//        return s != null ? s.name() : null;
//    }

}
