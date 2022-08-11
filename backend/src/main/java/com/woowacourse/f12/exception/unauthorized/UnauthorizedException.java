package com.woowacourse.f12.exception.unauthorized;

import com.woowacourse.f12.exception.CustomException;
import com.woowacourse.f12.exception.ErrorCode;

public class UnauthorizedException extends CustomException {

    public UnauthorizedException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
