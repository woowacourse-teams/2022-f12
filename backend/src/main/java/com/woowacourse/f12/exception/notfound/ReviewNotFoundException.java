package com.woowacourse.f12.exception.notfound;

import static com.woowacourse.f12.exception.ErrorCode.REVIEW_NOT_FOUND;

public class ReviewNotFoundException extends NotFoundException {

    public ReviewNotFoundException() {
        super(REVIEW_NOT_FOUND, "리뷰를 찾을 수 없습니다.");
    }
}
