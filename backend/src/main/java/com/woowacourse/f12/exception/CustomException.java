package com.woowacourse.f12.exception;

public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(final String message, final ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
