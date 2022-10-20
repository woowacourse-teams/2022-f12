package com.woowacourse.f12.exception.forbidden;

import com.woowacourse.f12.exception.CustomException;
import com.woowacourse.f12.exception.ErrorCode;

public class ForbiddenException extends CustomException {

    public ForbiddenException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
