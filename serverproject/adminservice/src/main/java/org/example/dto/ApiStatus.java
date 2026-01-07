package org.example.dto;

public enum ApiStatus {

    UNAUTHORIZED(401, "Xac thuc token ko hieu luc"),
    FORBIDDEN(403, "Khong co permission"),


    // Register
    REGISTER_SUCCESS(200, "Registration successful"),
    REGISTER_UNEXPECTED_ERROR(999, "Unexpected error"),
    REGISTER_TOO_MANY_REQUESTS(429, "Too many requests, please try later"),
    REGISTER_USERNAME_EXISTS(441, "Username already exists"),
    REGISTER_EMAIL_EXISTS(442, "Email already exists"),
    REGISTER_MISSING_INFO(443, "Missing required information"),


    REGISTER_VERIFY_SUCCESS(200, "Verification successful"),
    REGISTER_VERIFY_UNEXPECTED_ERROR(999, "Verification failed"),
    REGISTER_VERIFY_TRANSACTION_EXPIRED(444, "Transaction expired or not found"),
    REGISTER_VERIFY_INVALID_OTP(445, "Invalid OTP"),
    REGISTER_VERIFY_TOO_MANY_REQUESTS(429, "Too many requests, please try later"),

    RESEND_OTP_SUCCESS(200, "OTP resent successfully"),
    RESEND_OTP_NOT_FOUND(446, "TransactionId not found or expired"),
    RESEND_OTP_TRANSACTION_MISMATCH(447, "Old transactionId does not match"),
    RESEND_OTP_TOO_MANY_REQUESTS(429, "Too many requests, please try later"),

    // Login / Logout
    LOGIN_SUCCESS(200, "Login successful"),
    LOGIN_TOO_MANY_REQUESTS(429, "Login - Too many requests, please try later"),
    TWO_FA_REQUIRED(200, "2FA required"),

    LOGIN_FAIL(448, "Login failed"),

    LOGOUT_SUCCESS(200, "Logout successful"),
    LOGOUT_FAIL(449, "Logout failed"),

    REFRESH_SUCCESS(200, "Token refreshed successfully"),
    REFRESH_FAIL(450, "Failed to refresh token"),

    // Forgot password
    FORGOT_PASSWORD_SUCCESS(200, "Forgot password request successful"),
    FORGOT_PASSWORD_FAIL(452, "Forgot password request failed"),

    FORGOT_PASSWORD_VERIFY_SUCCESS(200, "Forgot password verification successful"),
    FORGOT_PASSWORD_VERIFY_FAIL(453, "Forgot password verification failed"),

    // Management / User
    USER_NOT_FOUND(460, "User not found"),
    USER_LIST_SUCCESS(200, "Fetched all users successfully"),
    USER_CREATE_SUCCESS(200, "User created successfully"),
    USER_CREATE_FAIL(461, "Failed to create user"),
    USER_UPDATE_SUCCESS(200, "User updated successfully"),
    USER_UPDATE_FAIL(462, "Failed to update user"),
    USER_DELETE_SUCCESS(200, "User deleted successfully"),
    USER_DELETE_FAIL(463, "Failed to delete user"),
    USER_LOCK_SUCCESS(200, "User locked successfully"),
    USER_LOCK_FAIL(464, "Failed to lock user"),


    USER_FETCH_SUCCESS(200, "Failed to lock user"),

    TWO_FA_SETUP_SUCCESS(200, "2FA setup successful"),
    TWO_FA_VERIFY_SUCCESS(200, "2FA enabled successfully"),
    TWO_FA_VERIFY_FAIL(466, "2FA verification failed");
    private final int code;
    private final String defaultMessage;

    ApiStatus(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public int getCode() { return code; }
    public String getDefaultMessage() { return defaultMessage; }

}