package com.woowacourse.f12.exception.unauthorized;

import static com.woowacourse.f12.exception.ErrorCode.EXPIRED_REFRESH_TOKEN;

public class RefreshTokenExpiredException extends UnauthorizedException {

    public RefreshTokenExpiredException() {
        super(EXPIRED_REFRESH_TOKEN, "리프레시 토큰이 만료됐습니다.");
    }
}
