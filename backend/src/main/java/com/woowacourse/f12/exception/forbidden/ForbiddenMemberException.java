package com.woowacourse.f12.exception.forbidden;

import com.woowacourse.f12.exception.CustomException;
import com.woowacourse.f12.exception.ExceptionCode;

public class ForbiddenMemberException extends CustomException {

    public ForbiddenMemberException(final ExceptionCode exceptionCode, final String message) {
        super(exceptionCode, message);
    }
}
