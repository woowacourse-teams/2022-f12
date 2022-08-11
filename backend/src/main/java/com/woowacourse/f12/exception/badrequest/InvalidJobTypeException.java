package com.woowacourse.f12.exception.badrequest;

import com.woowacourse.f12.exception.ExceptionCode;

public class InvalidJobTypeException extends InvalidValueException {

    public InvalidJobTypeException() {
        super(ExceptionCode.INVALID_SEARCH_PARAM, "올바르지 않은 직군 입력입니다.");
    }
}
