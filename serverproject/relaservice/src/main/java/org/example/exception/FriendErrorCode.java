package org.example.exception;

import lombok.Getter;

@Getter
public enum FriendErrorCode {
    FRIEND_REQUEST_ALREADY_EXISTS("FRIEND_001", "Friend request already exists"),
    FRIEND_REQUEST_NOT_FOUND("FRIEND_002", "Friend request not found"),
    INVALID_RELATIONSHIP_STATE("FRIEND_003", "Invalid relationship state"),
    FRIEND_REQUEST_INVALID_ACTION("FRIEND_004", "Invalid action on friend request"),
    FRIEND_REQUEST_SELF_NOT_ALLOWED("FRIEND_005", "Friend request self not allowed"),

    // Block related error codes
    BLOCK_SELF_NOT_ALLOWED("FRIEND_006", "Cannot block yourself"),
    USER_ALREADY_BLOCKED("FRIEND_007", "User is already blocked"),
    USER_NOT_BLOCKED("FRIEND_008", "User is not blocked"),
    CANNOT_PERFORM_ACTION_BLOCKED_USER("FRIEND_009", "Cannot perform this action on blocked user");

    private final String code;
    private final String message;

    FriendErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}