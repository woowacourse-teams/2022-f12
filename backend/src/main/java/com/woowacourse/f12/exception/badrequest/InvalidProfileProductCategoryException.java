package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ExceptionCode.INVALID_REQUEST_BODY;

public class InvalidProfileProductCategoryException extends InvalidValueException {

    public InvalidProfileProductCategoryException() {
        super(INVALID_REQUEST_BODY, "대표장비로 등록할 수 없는 카테고리입니다.");
    }
}
