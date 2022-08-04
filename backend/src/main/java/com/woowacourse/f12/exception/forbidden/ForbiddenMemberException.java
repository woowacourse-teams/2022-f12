package com.woowacourse.f12.exception.forbidden;

import com.woowacourse.f12.exception.CustomException;
import com.woowacourse.f12.exception.ErrorCode;

public class ForbiddenMemberException extends CustomException {

    public ForbiddenMemberException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
