package com.woowacourse.f12.exception.notfound;

import static com.woowacourse.f12.exception.ErrorCode.MEMBER_NOT_FOUND;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException() {
        super(MEMBER_NOT_FOUND, "회원을 찾을 수 없습니다.");
    }
}
