package com.woowacourse.f12.exception.unauthorized;

import static com.woowacourse.f12.exception.ErrorCode.EXPIRED_ACCESS_TOKEN;

public class TokenExpiredException extends UnauthorizedException {

    public TokenExpiredException() {
        super(EXPIRED_ACCESS_TOKEN, "토큰이 만료됐습니다.");
    }
}
