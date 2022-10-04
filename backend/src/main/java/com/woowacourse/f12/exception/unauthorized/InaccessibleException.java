package com.woowacourse.f12.exception.unauthorized;

import com.woowacourse.f12.exception.ErrorCode;

public class InaccessibleException extends UnauthorizedException {

    public InaccessibleException() {
        super(ErrorCode.INACCESSIBLE, "접근 권한이 없습니다.");
    }
}
