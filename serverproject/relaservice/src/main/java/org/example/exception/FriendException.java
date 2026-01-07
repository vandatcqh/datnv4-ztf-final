package org.example.exception;

import lombok.Getter;

@Getter
public class FriendException extends BaseException {
    private final FriendErrorCode friendErrorCode;

    public FriendException(FriendErrorCode friendErrorCode) {
        super(ErrorCode.INVALID_INPUT); // tạm reuse ErrorCode chung cho BaseException
        this.friendErrorCode = friendErrorCode;
    }

    public FriendException(FriendErrorCode friendErrorCode, Throwable cause) {
        super(ErrorCode.INVALID_INPUT, cause);
        this.friendErrorCode = friendErrorCode;
    }

    @Override
    public String getMessage() {
        return friendErrorCode.getMessage();
    }
}
