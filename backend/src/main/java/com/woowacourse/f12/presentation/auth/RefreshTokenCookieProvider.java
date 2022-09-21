package com.woowacourse.f12.presentation.auth;

import com.woowacourse.f12.exception.unauthorized.RefreshTokenNotExistException;
import java.time.Duration;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class RefreshTokenCookieProvider {

    protected static final String REFRESH_TOKEN = "refreshToken";
    private static final int REMOVAL_MAX_AGE = 0;

    private final Long expiredTimeMillis;

    public RefreshTokenCookieProvider(@Value("${security.refresh.expire-length}") final Long expiredTimeMillis) {
        this.expiredTimeMillis = expiredTimeMillis;
    }

    public void setCookie(final HttpServletResponse response, final String value) {
        final ResponseCookie responseCookie = createTokenCookieBuilder(value)
                .maxAge(Duration.ofMillis(expiredTimeMillis))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    public void removeCookie(final HttpServletRequest request, final HttpServletResponse response) {
        final Cookie cookie = WebUtils.getCookie(request, REFRESH_TOKEN);
        if (cookie == null) {
            throw new RefreshTokenNotExistException();
        }
        final ResponseCookie responseCookie = createTokenCookieBuilder(cookie.getValue())
                .maxAge(REMOVAL_MAX_AGE)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    private ResponseCookieBuilder createTokenCookieBuilder(final String value) {
        return ResponseCookie.from(REFRESH_TOKEN, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(SameSite.NONE.attributeValue());
    }
}
