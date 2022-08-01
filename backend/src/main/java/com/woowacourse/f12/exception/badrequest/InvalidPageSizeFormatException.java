package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_SEARCH_PARAM;

public class InvalidPageSizeFormatException extends InvalidValueException {

    public InvalidPageSizeFormatException() {
        super("패이지 크기는 숫자 형식이어야 합니다.", INVALID_SEARCH_PARAM);
    }
}
