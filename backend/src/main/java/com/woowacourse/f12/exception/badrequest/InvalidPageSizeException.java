package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_PAGING_PARAM;

public class InvalidPageSizeException extends InvalidValueException {

    public InvalidPageSizeException(int maxSize) {
        super(INVALID_PAGING_PARAM, "페이지의 크기는" + maxSize + "이하여야 합니다.");
    }
}
