package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_REQUEST_BODY;

public class DuplicatedProfileProductCategoryException extends InvalidValueException {

    public DuplicatedProfileProductCategoryException() {
        super("대표장비는 카테고리가 중복될 수 없습니다.", INVALID_REQUEST_BODY);
    }
}
