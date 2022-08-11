package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_SEARCH_PARAM;

public class InvalidJobTypeException extends InvalidValueException {

    public InvalidJobTypeException() {
        super(INVALID_SEARCH_PARAM, "올바르지 않은 직군 입력입니다.");
    }
}
