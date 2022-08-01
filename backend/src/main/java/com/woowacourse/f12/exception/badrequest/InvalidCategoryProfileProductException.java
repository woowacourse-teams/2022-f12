package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_REQUEST_BODY;

public class InvalidCategoryProfileProductException extends InvalidValueException {

    public InvalidCategoryProfileProductException() {
        super("대표장비로 등록할 수 없는 카테고리입니다.", INVALID_REQUEST_BODY);
    }
}
