package com.woowacourse.f12.application.auth;

import java.time.LocalDateTime;

public class RefreshTokenInfo {

    private final String memberId;
    private final LocalDateTime expiredAt;

    public RefreshTokenInfo(final String memberId, final LocalDateTime expiredAt) {
        this.memberId = memberId;
        this.expiredAt = expiredAt;
    }
}
