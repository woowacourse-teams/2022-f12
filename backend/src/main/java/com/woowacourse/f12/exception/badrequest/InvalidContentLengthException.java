package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_REVIEW_CONTENT_LENGTH;

public class InvalidContentLengthException extends InvalidValueException {

    public InvalidContentLengthException(final int maxLength) {
        super(INVALID_REVIEW_CONTENT_LENGTH, "내용의 길이는 " + maxLength + "자 이하 여야 합니다.");
    }
}
