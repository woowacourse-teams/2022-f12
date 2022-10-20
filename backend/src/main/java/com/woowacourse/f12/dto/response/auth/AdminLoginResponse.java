package com.woowacourse.f12.dto.response.auth;

import lombok.Getter;

@Getter
public class AdminLoginResponse {

    private final String accessToken;

    public AdminLoginResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
