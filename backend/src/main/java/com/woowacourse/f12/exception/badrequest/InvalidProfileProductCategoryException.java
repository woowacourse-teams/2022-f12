package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_CATEGORY_PROFILE_PRODUCT;

public class InvalidProfileProductCategoryException extends InvalidValueException {

    public InvalidProfileProductCategoryException() {
        super(INVALID_CATEGORY_PROFILE_PRODUCT, "대표장비로 등록할 수 없는 카테고리입니다.");
    }
}
