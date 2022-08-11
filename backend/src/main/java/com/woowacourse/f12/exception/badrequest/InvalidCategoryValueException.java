package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ExceptionCode.INVALID_SEARCH_PARAM;

public class InvalidCategoryValueException extends InvalidValueException {

    public InvalidCategoryValueException() {
        super(INVALID_SEARCH_PARAM, "잘못된 카테고리입니다.");
    }
}
