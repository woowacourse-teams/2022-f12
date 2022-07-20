package com.woowacourse.f12.exception;

public class ForbiddenMemberException extends RuntimeException {

    public ForbiddenMemberException(final String message) {
        super(message);
    }
}
