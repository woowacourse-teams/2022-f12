package com.woowacourse.f12.exception.badrequest;

public class InvalidValueException extends RuntimeException {

    public InvalidValueException(final String message) {
        super(message);
    }
}
