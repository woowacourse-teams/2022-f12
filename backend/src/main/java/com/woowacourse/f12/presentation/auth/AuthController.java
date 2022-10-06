package com.woowacourse.f12.presentation.auth;

import static com.woowacourse.f12.presentation.auth.RefreshTokenCookieProvider.REFRESH_TOKEN;

import com.woowacourse.f12.application.auth.AuthService;
import com.woowacourse.f12.dto.response.AccessTokenResponse;
import com.woowacourse.f12.dto.response.auth.AdminLoginResponse;
import com.woowacourse.f12.dto.response.auth.IssuedTokensResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.dto.result.LoginResult;
import com.woowacourse.f12.exception.unauthorized.RefreshTokenNotExistException;
import javax.servlet.http.HttpServletRequest;
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

    private final AuthService authService;
    private final RefreshTokenCookieProvider refreshTokenCookieProvider;

    public AuthController(final AuthService authService, final RefreshTokenCookieProvider refreshTokenCookieProvider) {
        this.authService = authService;
        this.refreshTokenCookieProvider = refreshTokenCookieProvider;
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponse> login(final HttpServletResponse response, @RequestParam final String code) {
        final LoginResult loginResult = authService.login(code);
        final String refreshToken = loginResult.getRefreshToken();
        refreshTokenCookieProvider.setCookie(response, refreshToken);
        return ResponseEntity.ok(LoginResponse.from(loginResult));
    }

    @GetMapping("/login/admin")
    public ResponseEntity<AdminLoginResponse> loginAdmin(@RequestParam final String code) {
        final AdminLoginResponse adminLoginResponse = authService.loginAdmin(code);
        return ResponseEntity.ok(adminLoginResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletRequest request, final HttpServletResponse response) {
        refreshTokenCookieProvider.removeCookie(request, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/accessToken")
    public ResponseEntity<AccessTokenResponse> issueAccessToken(final HttpServletRequest request,
                                                                final HttpServletResponse response,
                                                                @CookieValue(value = REFRESH_TOKEN, required = false) final String refreshToken) {
        if (refreshToken == null) {
            throw new RefreshTokenNotExistException();
        }
        final IssuedTokensResponse issuedTokensResponse = authService.issueAccessToken(refreshToken);
        refreshTokenCookieProvider.setCookie(response, issuedTokensResponse.getRefreshToken());
        return ResponseEntity.ok(new AccessTokenResponse(issuedTokensResponse.getAccessToken()));
    }
}
