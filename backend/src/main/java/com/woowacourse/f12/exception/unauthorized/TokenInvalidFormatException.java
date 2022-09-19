package com.woowacourse.f12.exception.unauthorized;

import static com.woowacourse.f12.exception.ErrorCode.INVALID_TOKEN_FORMAT;

public class TokenInvalidFormatException extends UnauthorizedException {

    public TokenInvalidFormatException() {
        super(INVALID_TOKEN_FORMAT, "토큰이 잘못된 형식입니다.");
    }
}
