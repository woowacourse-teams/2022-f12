package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.BLANK_REVIEW_CONTENT;

public class BlankContentException extends InvalidValueException {

    public BlankContentException() {
        super(BLANK_REVIEW_CONTENT, "리뷰 내용은 공백이 될 수 없습니다.");
    }
}
