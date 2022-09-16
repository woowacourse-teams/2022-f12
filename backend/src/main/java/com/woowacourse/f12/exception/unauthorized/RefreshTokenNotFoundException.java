package com.woowacourse.f12.exception.unauthorized;

import com.woowacourse.f12.exception.ErrorCode;

public class RefreshTokenNotFoundException extends UnauthorizedException {

    public RefreshTokenNotFoundException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_FOUND, "저장되지 않은 리프레시 토큰입니다.");
    }
}
