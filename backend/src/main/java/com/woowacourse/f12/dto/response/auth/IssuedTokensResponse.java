package com.woowacourse.f12.dto.response.auth;

public class IssuedTokensResponse {
    private String accessToken;
    private String refreshToken;

    private IssuedTokensResponse() {
    }

    public IssuedTokensResponse(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
