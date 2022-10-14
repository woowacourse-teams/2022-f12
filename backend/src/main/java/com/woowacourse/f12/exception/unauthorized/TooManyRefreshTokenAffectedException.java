package com.woowacourse.f12.exception.unauthorized;

import static com.woowacourse.f12.exception.ErrorCode.TOO_MANY_AFFECTED_REFRESH_TOKEN;

public class TooManyRefreshTokenAffectedException extends UnauthorizedException {
    public TooManyRefreshTokenAffectedException() {
        super(TOO_MANY_AFFECTED_REFRESH_TOKEN, "예상보다 더 많은 리프레시 토큰이 서버에 반영됐습니다.");
    }
}
