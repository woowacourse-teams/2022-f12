package com.woowacourse.f12.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public CustomException(final ExceptionCode exceptionCode, final String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
