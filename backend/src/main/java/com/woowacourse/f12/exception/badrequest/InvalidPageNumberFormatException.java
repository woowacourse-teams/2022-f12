package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_PAGING_PARAM;

public class InvalidPageNumberFormatException extends InvalidValueException {

    public InvalidPageNumberFormatException() {
        super(INVALID_PAGING_PARAM, "페이지 번호는 숫자 형식이여야 합니다.");
    }
}
