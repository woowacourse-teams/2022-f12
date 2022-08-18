package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.CATEGORY_DUPLICATED_PROFILE_PRODUCT;

public class DuplicatedProfileProductCategoryException extends InvalidValueException {

    public DuplicatedProfileProductCategoryException() {
        super(CATEGORY_DUPLICATED_PROFILE_PRODUCT, "대표장비는 카테고리가 중복될 수 없습니다.");
    }
}
