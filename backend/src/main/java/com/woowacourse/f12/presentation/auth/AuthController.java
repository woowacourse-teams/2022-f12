package com.woowacourse.f12.presentation.auth;

import com.woowacourse.f12.application.auth.AuthService;
import com.woowacourse.f12.dto.response.AccessTokenResponse;
import com.woowacourse.f12.dto.response.auth.IssuedTokensResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.response.auth.TokenResponse;
import com.woowacourse.f12.exception.unauthorized.RefreshTokenNotExistException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private static final String REFRESH_TOKEN = "refreshToken";
    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam final String code, final HttpServletResponse response) {
        final TokenResponse tokenResponse = authService.login(code);
        final String refreshToken = tokenResponse.getRefreshToken();
        setRefreshToken(response, refreshToken);
        return ResponseEntity.ok(tokenResponse.getLoginResponse());
    }

    private void setRefreshToken(final HttpServletResponse response, final String refreshToken) {
        final Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    @PostMapping("/accessToken")
    public ResponseEntity<AccessTokenResponse> issueAccessToken(final HttpServletResponse response,
                                                                @CookieValue(value = REFRESH_TOKEN, required = false) final String refreshToken) {
        if (refreshToken == null) {
            throw new RefreshTokenNotExistException();
        }
        final IssuedTokensResponse issuedTokensResponse = authService.issueAccessToken(refreshToken);
        setRefreshToken(response, issuedTokensResponse.getRefreshToken());
        return ResponseEntity.ok(new AccessTokenResponse(issuedTokensResponse.getAccessToken()));
    }
}
