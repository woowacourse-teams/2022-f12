package com.woowacourse.f12.exception.forbidden;

import static com.woowacourse.f12.exception.ErrorCode.PERMISSION_DENIED;

public class NotAuthorException extends ForbiddenException {

    public NotAuthorException() {
        super(PERMISSION_DENIED, "리뷰의 작성자가 아닙니다.");
    }
}
