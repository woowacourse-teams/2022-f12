package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_REQUEST_BODY;

public class InvalidContentLengthException extends InvalidValueException {

    public InvalidContentLengthException(final int maxLength) {
        super("내용의 길이는 " + maxLength + "자 이하 여야 합니다.", INVALID_REQUEST_BODY);
    }
}
