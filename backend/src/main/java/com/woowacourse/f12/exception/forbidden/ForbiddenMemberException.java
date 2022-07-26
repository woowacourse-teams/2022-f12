package com.woowacourse.f12.exception.forbidden;

public class ForbiddenMemberException extends RuntimeException {

    public ForbiddenMemberException(final String message) {
        super(message);
    }
}
