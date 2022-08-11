package com.woowacourse.f12.exception.unauthorized;

import com.woowacourse.f12.exception.CustomException;
import com.woowacourse.f12.exception.ExceptionCode;

public class UnauthorizedException extends CustomException {

    public UnauthorizedException(final ExceptionCode exceptionCode, final String message) {
        super(exceptionCode, message);
    }
}
