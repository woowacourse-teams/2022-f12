package com.woowacourse.f12.application.auth;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenProvider {

    private final long expireLength;

    public RefreshTokenProvider(@Value("${security.refresh.expire-length}") final long expireLength) {
        this.expireLength = expireLength;
    }

    public RefreshToken createToken(final Long memberId) {
        final int days = (int) TimeUnit.MILLISECONDS.toDays(expireLength);
        final LocalDateTime expireDateTime = LocalDateTime.now().plusDays(days);
        return new RefreshToken(UuidUtil.getTimeBasedUuid().toString(), memberId, expireDateTime);
    }
}
