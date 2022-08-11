package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ExceptionCode.INVALID_SEARCH_PARAM;

public class InvalidCareerLevelException extends InvalidValueException {

    public InvalidCareerLevelException() {
        super(INVALID_SEARCH_PARAM, "올바르지 않은 연차 입력입니다.");
    }
}
