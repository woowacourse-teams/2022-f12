package com.woowacourse.f12.exception.internalserver;

public class ExternalServerException extends RuntimeException {

    public ExternalServerException(final String message) {
        super(message);
    }
}
