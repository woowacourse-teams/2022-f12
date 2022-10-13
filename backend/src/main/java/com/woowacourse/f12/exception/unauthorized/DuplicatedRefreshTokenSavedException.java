package com.woowacourse.f12.exception.unauthorized;

import com.woowacourse.f12.exception.ErrorCode;

public class DuplicatedRefreshTokenSavedException extends UnauthorizedException {

    public DuplicatedRefreshTokenSavedException() {
        super(ErrorCode.DUPLICATED_REFRESH_TOKEN, "서버에 조회하려는 리프레시 토큰이 2개 이상입니다.");
    }
}
