package com.woowacourse.f12.exception.unauthorized;

import static com.woowacourse.f12.exception.ErrorCode.NOT_EXIST_TOKEN;

public class TokenNotExistsException extends UnauthorizedException {

    public TokenNotExistsException() {
        super(NOT_EXIST_TOKEN, "토큰이 존재하지 않습니다.");
    }
}
