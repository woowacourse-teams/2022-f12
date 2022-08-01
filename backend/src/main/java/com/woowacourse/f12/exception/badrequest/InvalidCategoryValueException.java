package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_SEARCH_PARAM;

public class InvalidCategoryValueException extends InvalidValueException {

    public InvalidCategoryValueException() {
        super("잘못된 카테고리입니다.", INVALID_SEARCH_PARAM);
    }
}
