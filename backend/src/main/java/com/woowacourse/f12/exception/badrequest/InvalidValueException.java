package com.woowacourse.f12.exception.badrequest;

import com.woowacourse.f12.exception.CustomException;
import com.woowacourse.f12.exception.ErrorCode;

public class InvalidValueException extends CustomException {

    public InvalidValueException(final String message, final ErrorCode errorCode) {
        super(message, errorCode);
    }
}
