package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_PROFILE_PRODUCT_UPDATE;

public class InvalidProfileProductUpdateException extends InvalidValueException {

    public InvalidProfileProductUpdateException() {
        super(INVALID_PROFILE_PRODUCT_UPDATE, "인벤토리에 포함되지 않은 제품을 대표 장비로 등록할 수 없습니다.");
    }
}
