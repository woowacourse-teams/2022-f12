package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.SELF_FOLLOW;

public class SelfFollowException extends InvalidValueException {

    public SelfFollowException() {
        super(SELF_FOLLOW, "자기 자신을 팔로우할 수 없습니다.");
    }
}
