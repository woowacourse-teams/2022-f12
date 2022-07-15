package com.woowacourse.f12.dto.response;

import com.woowacourse.f12.domain.Member;
import lombok.Getter;

@Getter
public class LoginResponse {

    private String token;
    private MemberResponse member;

    private LoginResponse() {
    }

    private LoginResponse(final String token, final MemberResponse member) {
        this.token = token;
        this.member = member;
    }

    public static LoginResponse of(final String token, final Member member) {
        final MemberResponse memberResponse = MemberResponse.from(member);
        return new LoginResponse(token, memberResponse);
    }
}
