package com.woowacourse.f12.exception.unauthorized;

public class DuplicatedRefreshTokenSavedException extends UnauthorizedException {

    public DuplicatedRefreshTokenSavedException() {
        super(null, "서버에 조회하려는 리프레시 토큰이 2개 이상입니다.");
    }
}
