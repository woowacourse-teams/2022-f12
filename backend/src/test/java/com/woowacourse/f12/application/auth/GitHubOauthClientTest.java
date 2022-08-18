package com.woowacourse.f12.application.auth;

import static com.woowacourse.f12.support.fixture.GitHubProfileFixtures.CORINNE_GITHUB;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.response.auth.GitHubProfileResponse;
import com.woowacourse.f12.support.fixture.MemberFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class GitHubOauthClientTest {

    @Autowired
    private GitHubOauthClient gitHubOauthClient;

    @Test
    void GitHub에_accessToken을_요청한다() {
        // given, when
        String accessToken = gitHubOauthClient.getAccessToken(CORINNE_GITHUB.getCode());

        // then
        assertThat(accessToken).isEqualTo(CORINNE_GITHUB.getToken());
    }

    @Test
    void GitHub에_프로필을_요청한다() {
        // given
        Member corinne = MemberFixtures.CORINNE.생성();
        String accessToken = CORINNE_GITHUB.getToken();
        GitHubProfileResponse expected = new GitHubProfileResponse(corinne.getGitHubId(), corinne.getName(),
                corinne.getImageUrl());

        // when
        GitHubProfileResponse gitHubProfileResponse = gitHubOauthClient.getProfile(accessToken);

        // then
        assertThat(gitHubProfileResponse).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
