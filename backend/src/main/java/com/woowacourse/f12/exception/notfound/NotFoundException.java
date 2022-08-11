package com.woowacourse.f12.exception.notfound;

import com.woowacourse.f12.exception.CustomException;
import com.woowacourse.f12.exception.ExceptionCode;

public class NotFoundException extends CustomException {

    public NotFoundException(final ExceptionCode exceptionCode, final String message) {
        super(exceptionCode, message);
    }
}
