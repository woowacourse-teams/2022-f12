package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.NOT_FOLLOWING;

public class NotFollowingException extends InvalidValueException {

    public NotFollowingException() {
        super(NOT_FOLLOWING, "팔로잉하고 있지 않습니다.");
    }
}
