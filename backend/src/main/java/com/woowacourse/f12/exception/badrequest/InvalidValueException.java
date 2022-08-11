package com.woowacourse.f12.exception.badrequest;

import com.woowacourse.f12.exception.CustomException;
import com.woowacourse.f12.exception.ExceptionCode;

public class InvalidValueException extends CustomException {

    public InvalidValueException(final ExceptionCode exceptionCode, final String message) {
        super(exceptionCode, message);
    }
}
