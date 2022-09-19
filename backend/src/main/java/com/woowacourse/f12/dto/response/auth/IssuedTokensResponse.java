package com.woowacourse.f12.dto.response.auth;

import com.woowacourse.f12.application.auth.RefreshToken;
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

    public static IssuedTokensResponse of(final String accessToken, final RefreshToken refreshToken) {
        return new IssuedTokensResponse(accessToken, refreshToken.getRefreshToken());
    }
}
