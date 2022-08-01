package com.woowacourse.f12.exception.notfound;

import com.woowacourse.f12.exception.CustomException;
import com.woowacourse.f12.exception.ErrorCode;

public class NotFoundException extends CustomException {

    public NotFoundException(final String message, final ErrorCode errorCode) {
        super(errorCode, message);
    }
}
