package com.woowacourse.f12.exception.internalserver;

import com.woowacourse.f12.exception.CustomException;
import com.woowacourse.f12.exception.ErrorCode;

public class ExternalServerException extends CustomException {

    public ExternalServerException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
