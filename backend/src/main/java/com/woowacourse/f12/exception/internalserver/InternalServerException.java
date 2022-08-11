package com.woowacourse.f12.exception.internalserver;

import com.woowacourse.f12.exception.CustomException;
import com.woowacourse.f12.exception.ExceptionCode;

public class InternalServerException extends CustomException {

    public InternalServerException(final ExceptionCode exceptionCode, final String message) {
        super(exceptionCode, message);
    }
}
