package com.woowacourse.f12.dto.response;

import com.woowacourse.f12.domain.Member;
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

    public static LoginResponse of(final String token, final Member member) {
        final LoginMemberResponse loginMemberResponse = LoginMemberResponse.from(member);
        return new LoginResponse(token, member.isRegisterCompleted(), loginMemberResponse);
    }
}
