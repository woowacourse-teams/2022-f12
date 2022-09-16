package com.woowacourse.f12.exception.unauthorized;

import com.woowacourse.f12.exception.ErrorCode;

public class RefreshTokenNotExistException extends UnauthorizedException {

    public RefreshTokenNotExistException() {
        super(ErrorCode.NOT_EXIST_REFRESH_TOKEN, "리프레시 토큰이 존재하지 않습니다.");
    }
}
