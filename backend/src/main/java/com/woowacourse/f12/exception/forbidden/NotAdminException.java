package com.woowacourse.f12.exception.forbidden;

import static com.woowacourse.f12.exception.ErrorCode.PERMISSION_DENIED;

public class NotAdminException extends ForbiddenException {

    public NotAdminException() {
        super(PERMISSION_DENIED, "관리자가 아닙니다.");
    }
}
