package com.woowacourse.f12.exception.badrequest;

import com.woowacourse.f12.exception.ErrorCode;

public class InvalidUrlLengthException extends InvalidValueException {
    public InvalidUrlLengthException(final int maxLength) {
        super(ErrorCode.INVALID_URL_LENGTH, String.format("요청 URL의 길이가 최대 길이(%d자)를 넘을 수 없습니다.", maxLength));
    }
}
