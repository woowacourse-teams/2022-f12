package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_FOLLOWER_COUNT;
import static com.woowacourse.f12.exception.ErrorCode.INVALID_LOGIN_CODE;

public class InvalidFollowerCountException extends InvalidValueException {

    public InvalidFollowerCountException() {
        super(INVALID_FOLLOWER_COUNT, "팔로워 카운트는 0보다 작을 수 없습니다.");
    }
}
