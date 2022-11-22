package com.woowacourse.f12.presentation.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.time.Duration;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

class RefreshTokenCookieProviderTest {

    private HttpServletRequest request;
    private RefreshTokenCookieProvider provider;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
    }

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
        String refreshTokenValue = "refreshTokenValue";
        Cookie cookie = new Cookie("refreshToken", refreshTokenValue);
        given(request.getCookies()).willReturn(new Cookie[]{cookie});

        // when
        ResponseCookie responseCookie = provider.expireCookie(cookie);

        // then
        assertThat(responseCookie.getMaxAge()).isZero();
    }
}
