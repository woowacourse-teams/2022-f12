package com.woowacourse.f12.presentation.auth;

import com.woowacourse.f12.exception.unauthorized.RefreshTokenNotExistException;
import java.time.Duration;
import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCookieProvider {

    protected static final String REFRESH_TOKEN = "refreshToken";
    private static final int REMOVAL_MAX_AGE = 0;

    private final Long expiredTimeMillis;

    public RefreshTokenCookieProvider(@Value("${security.refresh.expire-length}") final Long expiredTimeMillis) {
        this.expiredTimeMillis = expiredTimeMillis;
    }

    public ResponseCookie createCookie(final String refreshToken) {
        return createTokenCookieBuilder(refreshToken)
                .maxAge(Duration.ofMillis(expiredTimeMillis))
                .build();
    }

    public ResponseCookie expireCookie(final Cookie cookie) {
        if (cookie == null) {
            throw new RefreshTokenNotExistException();
        }
        return createTokenCookieBuilder(cookie.getValue())
                .maxAge(REMOVAL_MAX_AGE)
                .build();
    }

    private ResponseCookieBuilder createTokenCookieBuilder(final String value) {
        return ResponseCookie.from(REFRESH_TOKEN, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(SameSite.NONE.attributeValue());
    }
}
