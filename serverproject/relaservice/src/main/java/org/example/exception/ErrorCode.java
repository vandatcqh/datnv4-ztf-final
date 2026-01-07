package org.example.exception;

public enum ErrorCode {
    // Authentication errors
    INVALID_CREDENTIALS("AUTH_001", "Invalid credentials"),
    USER_NOT_FOUND("AUTH_002", "User not found"),
    ACCOUNT_LOCKED("AUTH_003", "Account is locked"),
    TOKEN_EXPIRED("AUTH_004", "Token expired"),
    INVALID_TOKEN("AUTH_005", "Invalid token"),
    EMAIL_NOT_FOUND("AUTH_006", "Email not found"),


    //Users profile errors
    INVALID_INPUT("USER_001", "Invalid input data"),

    // Rate limiting
    TOO_MANY_REQUESTS("RATE_001", "Too many requests"),

    // Server errors
    INTERNAL_SERVER_ERROR("SYS_001", "Internal server error");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}