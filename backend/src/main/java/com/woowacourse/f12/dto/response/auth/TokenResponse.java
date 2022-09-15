package com.woowacourse.f12.dto.response.auth;

public class TokenResponse {

    private final String refreshToken;
    private final LoginResponse loginResponse;

    public TokenResponse(final String refreshToken, final LoginResponse loginResponse) {
        this.refreshToken = refreshToken;
        this.loginResponse = loginResponse;
    }

    public String getAccessToken() {
        return loginResponse.getToken();
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }
}
