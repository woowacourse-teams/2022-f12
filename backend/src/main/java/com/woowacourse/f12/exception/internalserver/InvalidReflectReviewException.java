package com.woowacourse.f12.exception.internalserver;

import com.woowacourse.f12.exception.ErrorCode;

public class InvalidReflectReviewException extends InternalServerException {

    public InvalidReflectReviewException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR, "리뷰 대상이 아닌 제품에 리뷰 정보를 반영하려 하고 있습니다.");
    }
}
