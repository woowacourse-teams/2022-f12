package com.woowacourse.f12.presentation.auth;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

class RefreshTokenCookieProviderTest {

    private static final int PER_SECOND = 1000;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RefreshTokenCookieProvider provider;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void 쿠키를_응답헤더에_세팅한다() {
        // given
        final long expiredTimeMillis = 1000L;
        provider = new RefreshTokenCookieProvider(expiredTimeMillis);
        final String refreshTokenValue = "refreshTokenValue";

        // when
        provider.setCookie(response, refreshTokenValue);

        // then
        verify(response)
                .addHeader(eq(HttpHeaders.SET_COOKIE), argThat(
                        argument -> argument.contains("refreshToken=" + refreshTokenValue)
                                && argument.contains("Path=/")
                                && argument.contains("Secure")
                                && argument.contains("HttpOnly")
                                && argument.contains("SameSite=None")
                                && argument.contains("Max-Age=" + expiredTimeMillis / PER_SECOND))
                );
    }

    @Test
    void 쿠키_제거를_응답헤더에_세팅한다() {
        // given
        provider = new RefreshTokenCookieProvider(1000L);
        String refreshTokenValue = "refreshTokenValue";
        Cookie cookie = new Cookie("refreshToken", refreshTokenValue);
        given(request.getCookies()).willReturn(new Cookie[]{cookie});

        // when
        provider.removeCookie(request, response);

        // then
        assertAll(
                () -> verify(request).getCookies(),
                () -> verify(response)
                        .addHeader(eq(HttpHeaders.SET_COOKIE), argThat(
                                argument -> argument.contains("refreshToken=" + refreshTokenValue)
                                        && argument.contains("Path=/")
                                        && argument.contains("Secure")
                                        && argument.contains("HttpOnly")
                                        && argument.contains("SameSite=None")
                                        && argument.contains("Max-Age=0"))
                        )
        );
    }
}
