package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ExceptionCode.DUPLICATED_DATA;

public class AlreadyWrittenReviewException extends InvalidValueException {

    public AlreadyWrittenReviewException() {
        super(DUPLICATED_DATA, "해당 제품에 대해 이미 리뷰가 작성되어 있습니다.");
    }
}
