package com.woowacourse.f12.exception.unauthorized;

import static com.woowacourse.f12.exception.ErrorCode.TOKEN_INVALID;

public class TokenInvalidException extends UnauthorizedException {

    public TokenInvalidException() {
        super("토큰이 잘못된 형식입니다.", TOKEN_INVALID);
    }
}
