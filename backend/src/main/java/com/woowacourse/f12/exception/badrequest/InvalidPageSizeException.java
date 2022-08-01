package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_SEARCH_PARAM;

public class InvalidPageSizeException extends InvalidValueException {

    public InvalidPageSizeException(int maxSize) {
        super("페이지의 크기는" + maxSize + "이하여야 합니다.", INVALID_SEARCH_PARAM);
    }
}
