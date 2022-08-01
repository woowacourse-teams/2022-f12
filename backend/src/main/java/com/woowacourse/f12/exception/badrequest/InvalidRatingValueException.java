package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_REQUEST_BODY;

public class InvalidRatingValueException extends InvalidValueException {

    public InvalidRatingValueException() {
        super("평점은 1에서 5 사이여야 합니다.", INVALID_REQUEST_BODY);
    }
}
