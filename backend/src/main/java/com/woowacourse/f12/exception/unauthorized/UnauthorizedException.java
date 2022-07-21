package com.woowacourse.f12.exception.unauthorized;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("로그인이 필요합니다.");
    }
}
