package com.woowacourse.f12.exception.forbidden;

import com.woowacourse.f12.exception.CustomException;
import com.woowacourse.f12.exception.ErrorCode;

public class ForbiddenMemberException extends CustomException {

    public ForbiddenMemberException(final String message, final ErrorCode errorCode) {
        super(message, errorCode);
    }
}
