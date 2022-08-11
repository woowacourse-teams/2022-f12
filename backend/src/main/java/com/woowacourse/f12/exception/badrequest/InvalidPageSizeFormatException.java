package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ExceptionCode.INVALID_SEARCH_PARAM;

public class InvalidPageSizeFormatException extends InvalidValueException {

    public InvalidPageSizeFormatException() {
        super(INVALID_SEARCH_PARAM, "패이지 크기는 숫자 형식이어야 합니다.");
    }
}
