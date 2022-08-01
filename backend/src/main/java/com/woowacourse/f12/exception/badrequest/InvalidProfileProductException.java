package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_REQUEST_BODY;

public class InvalidProfileProductException extends InvalidValueException {

    public InvalidProfileProductException() {
        super("유효하지 않은 대표 장비 수정 요청입니다.", INVALID_REQUEST_BODY);
    }
}
