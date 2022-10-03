package com.woowacourse.f12.application.auth.token;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;

@Getter
public class RefreshToken {

    private final String refreshToken;
    private final Long memberId;
    private final LocalDateTime expiredAt;

    public RefreshToken(final String refreshToken, final Long memberId, final LocalDateTime expiredAt) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.expiredAt = expiredAt;
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
        final RefreshToken that = (RefreshToken) o;
        return Objects.equals(refreshToken, that.refreshToken) && Objects.equals(memberId,
                that.memberId) && Objects.equals(expiredAt, that.expiredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refreshToken, memberId, expiredAt);
    }
}
