package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_REVIEW_RATING;

public class InvalidRatingValueException extends InvalidValueException {

    public InvalidRatingValueException() {
        super(INVALID_REVIEW_RATING, "평점은 1에서 5 사이여야 합니다.");
    }
}
