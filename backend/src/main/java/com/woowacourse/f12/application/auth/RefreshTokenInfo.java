package com.woowacourse.f12.application.auth;

import java.time.LocalDateTime;
import java.util.Objects;

public class RefreshTokenInfo {

    private final Long memberId;
    private final LocalDateTime expiredAt;

    private RefreshTokenInfo(final Long memberId, final LocalDateTime expiredAt) {
        this.memberId = memberId;
        this.expiredAt = expiredAt;
    }

    public static RefreshTokenInfo createByExpiredDay(final Long memberId, final int days) {
        return new RefreshTokenInfo(memberId, LocalDateTime.now().plusDays(days));
    }

    public Long getMemberId() {
        return memberId;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RefreshTokenInfo that = (RefreshTokenInfo) o;
        return Objects.equals(memberId, that.memberId) && Objects.equals(expiredAt, that.expiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, expiredAt);
    }
}
