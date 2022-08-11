package com.woowacourse.f12.exception.unauthorized;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_TOKEN;

public class TokenInvalidException extends UnauthorizedException {

    public TokenInvalidException() {
        super(INVALID_TOKEN, "토큰이 잘못된 형식입니다.");
    }
}
