package com.woowacourse.f12.exception.badrequest;

import static com.woowacourse.f12.exception.ErrorCode.ALREADY_FOLLOWING;

public class AlreadyFollowingException extends InvalidValueException {

    public AlreadyFollowingException() {
        super(ALREADY_FOLLOWING, "이미 팔로우하고 있는 회원입니다.");
    }
}
