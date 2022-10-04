package com.woowacourse.f12.support.fixture;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.support.fixture.GitHubProfileFixture.CORINNE_GITHUB;
import static com.woowacourse.f12.support.fixture.GitHubProfileFixture.KLAY_GITHUB;
import static com.woowacourse.f12.support.fixture.GitHubProfileFixture.MINCHO_GITHUB;
import static com.woowacourse.f12.support.fixture.GitHubProfileFixture.OHZZI_GITHUB;

import com.woowacourse.f12.dto.response.auth.LoginResponse;
import com.woowacourse.f12.support.action.AuthorizedAction;

public enum AcceptanceFixture {

    코린(CORINNE_GITHUB.getCode(), MemberFixture.CORINNE),
    민초(MINCHO_GITHUB.getCode(), MemberFixture.MINCHO),
    오찌(OHZZI_GITHUB.getCode(), MemberFixture.OHZZI),
    클레이(KLAY_GITHUB.getCode(), MemberFixture.ADMIN_KLAY),
    ;
    private final String gitHubLoginCode;
    private final MemberFixture memberFixture;

    AcceptanceFixture(final String gitHubLoginCode, final MemberFixture memberFixture) {
        this.gitHubLoginCode = gitHubLoginCode;
        this.memberFixture = memberFixture;
    }

    public MemberFixture 엔티티를() {
        return memberFixture;
    }

    public LoginResponse 로그인을_한다() {
        return GET_요청을_보낸다("/api/v1/login?code=" + gitHubLoginCode)
                .as(LoginResponse.class);
    }

    public AuthorizedAction 로그인을_하고() {
        String token = GET_요청을_보낸다("/api/v1/login?code=" + gitHubLoginCode)
                .as(LoginResponse.class)
                .getToken();
        return new AuthorizedAction(token);
    }

    public AuthorizedAction 로그인한_상태로(final String token) {
        return new AuthorizedAction(token);
    }
}
