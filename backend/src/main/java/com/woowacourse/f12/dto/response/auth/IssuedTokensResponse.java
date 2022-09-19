package com.woowacourse.f12.dto.response.auth;

import lombok.Getter;

@Getter
public class IssuedTokensResponse {

    private String accessToken;
    private String refreshToken;

    private IssuedTokensResponse() {
    }

    public IssuedTokensResponse(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
