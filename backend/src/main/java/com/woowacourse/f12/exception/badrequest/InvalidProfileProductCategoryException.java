package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.PROFILE_CONTAINS_SOFTWARE;

public class InvalidProfileProductCategoryException extends InvalidValueException {

    public InvalidProfileProductCategoryException() {
        super(PROFILE_CONTAINS_SOFTWARE, "대표장비로 등록할 수 없는 카테고리입니다.");
    }
}
