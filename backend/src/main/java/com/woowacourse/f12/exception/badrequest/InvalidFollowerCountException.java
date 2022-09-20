package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_LOGIN_CODE;

public class InvalidFollowerCountException extends InvalidValueException {

    public InvalidFollowerCountException() {
        super(INVALID_LOGIN_CODE, "팔로워 카운트는 0보다 작을 수 없습니다.");
    }
}
