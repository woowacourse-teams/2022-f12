package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_REQUEST_BODY;

public class InvalidProfileProductException extends InvalidValueException {

    public InvalidProfileProductException() {
        super(INVALID_REQUEST_BODY, "유효하지 않은 대표 장비 수정 요청입니다.");
    }
}
