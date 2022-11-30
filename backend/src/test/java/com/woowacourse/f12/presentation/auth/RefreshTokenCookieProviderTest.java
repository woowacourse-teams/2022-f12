package com.woowacourse.f12.presentation.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

class RefreshTokenCookieProviderTest {

    private RefreshTokenCookieProvider provider;

    @Test
    void 리프레시_토큰으로_쿠키를_생성한다() {
        // given
        final long expiredTimeMillis = 1000L;
        provider = new RefreshTokenCookieProvider(expiredTimeMillis);
        final String refreshTokenValue = "refreshTokenValue";

        // when
        ResponseCookie responseCookie = provider.createCookie(refreshTokenValue);

        // then
        assertAll(
                () -> assertThat(responseCookie.getValue()).isEqualTo(refreshTokenValue),
                () -> assertThat(responseCookie.getMaxAge()).isEqualTo(Duration.ofMillis(expiredTimeMillis))
        );
    }

    @Test
    void 생성된_쿠키를_받아서_만료시킨_뒤_반환한다() {
        // given
        provider = new RefreshTokenCookieProvider(1000L);

        // when
        ResponseCookie responseCookie = provider.createLogoutCookie();

        // then
        assertThat(responseCookie.getMaxAge()).isZero();
    }
}
