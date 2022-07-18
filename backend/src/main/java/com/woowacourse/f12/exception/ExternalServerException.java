package com.woowacourse.f12.exception;

public class ExternalServerException extends RuntimeException {

    public ExternalServerException(final String message) {
        super(message);
    }
}
