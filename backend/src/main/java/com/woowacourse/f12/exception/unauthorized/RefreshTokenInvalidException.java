package com.woowacourse.f12.exception.unauthorized;

import com.woowacourse.f12.exception.ErrorCode;

public class RefreshTokenInvalidException extends UnauthorizedException {

    public RefreshTokenInvalidException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_FOUND, "유효하지 않은 리프레시 토큰입니다.");
    }
}
