package com.woowacourse.f12.exception.unauthorized;

import static com.woowacourse.f12.exception.ErrorCode.TOKEN_NOT_EXISTS;

public class TokenNotExistsException extends UnauthorizedException {

    public TokenNotExistsException() {
        super("토큰이 존재하지 않습니다.", TOKEN_NOT_EXISTS);
    }
}