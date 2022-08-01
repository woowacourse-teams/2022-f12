package com.woowacourse.f12.exception.badrequest;

import com.woowacourse.f12.exception.ErrorCode;

public class InvalidJobTypeException extends InvalidValueException {

    public InvalidJobTypeException() {
        super("올바르지 않은 직군 입력입니다.", ErrorCode.INVALID_SEARCH_PARAM);
    }
}
