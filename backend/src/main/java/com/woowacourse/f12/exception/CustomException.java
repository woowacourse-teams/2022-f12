package com.woowacourse.f12.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
