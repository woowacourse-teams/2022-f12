package com.woowacourse.f12.presentation.auth;

import static com.woowacourse.f12.presentation.auth.RefreshTokenCookieProvider.REFRESH_TOKEN;

import com.woowacourse.f12.application.auth.AuthService;
import com.woowacourse.f12.dto.response.AccessTokenResponse;
import com.woowacourse.f12.dto.response.auth.AdminLoginResponse;
import com.woowacourse.f12.dto.response.auth.IssuedTokensResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.result.LoginResult;
import com.woowacourse.f12.exception.unauthorized.RefreshTokenNotExistException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

    private final AuthService authService;
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;

    public AuthController(final AuthService authService, final RefreshTokenCookieProvider refreshTokenCookieProvider) {
        this.authService = authService;
        this.refreshTokenCookieProvider = refreshTokenCookieProvider;
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam final String code) {
        final LoginResult loginResult = authService.login(code);
        final String refreshToken = loginResult.getRefreshToken();
        ResponseCookie cookie = refreshTokenCookieProvider.createCookie(refreshToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(LoginResponse.from(loginResult));
    }

    @GetMapping("/login/admin")
    public AdminLoginResponse loginAdmin(@RequestParam final String code) {
        return authService.loginAdmin(code);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(value = REFRESH_TOKEN, required = false) final String refreshToken) {
        validateRefreshTokenExists(refreshToken);
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookieProvider.createLogoutCookie().toString())
                .build();
    }

    @PostMapping("/accessToken")
    public ResponseEntity<AccessTokenResponse> issueAccessToken(
            @CookieValue(value = REFRESH_TOKEN, required = false) final String refreshToken) {
        validateRefreshTokenExists(refreshToken);
        final IssuedTokensResponse issuedTokensResponse = authService.issueAccessToken(refreshToken);
        final ResponseCookie responseCookie = refreshTokenCookieProvider.createCookie(
                issuedTokensResponse.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(new AccessTokenResponse(issuedTokensResponse.getAccessToken()));
    }

    private void validateRefreshTokenExists(final String refreshToken) {
        if (refreshToken == null) {
            throw new RefreshTokenNotExistException();
        }
    }
}
