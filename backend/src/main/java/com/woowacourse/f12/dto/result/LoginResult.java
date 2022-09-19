package com.woowacourse.f12.dto.result;

import com.woowacourse.f12.domain.member.Member;
import lombok.Getter;

@Getter
public class LoginResult {

    private final String refreshToken;
    private final String accessToken;
    private final Member member;

    public LoginResult(final String refreshToken, final String accessToken, final Member member) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
        this.member = member;
    }
}
