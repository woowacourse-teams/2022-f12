package com.woowacourse.f12.dto.response.auth;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.result.LoginResult;
import lombok.Getter;

@Getter
public class LoginResponse {

    private String token;
    private boolean registerCompleted;
    private LoginMemberResponse member;

    private LoginResponse() {
    }

    public LoginResponse(final String token, final boolean registerCompleted,
                         final LoginMemberResponse loginMemberResponse) {
        this.token = token;
        this.registerCompleted = registerCompleted;
        this.member = loginMemberResponse;
    }

    public static LoginResponse from(final LoginResult loginResult) {
        final Member member = loginResult.getMember();
        final LoginMemberResponse loginMemberResponse = LoginMemberResponse.from(member);
        return new LoginResponse(loginResult.getAccessToken(), member.isRegistered(), loginMemberResponse);
    }
}
