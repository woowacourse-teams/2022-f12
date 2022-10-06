package com.woowacourse.f12.application.auth;

import static com.woowacourse.f12.support.fixture.GitHubProfileFixture.CORINNE_GITHUB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.response.auth.GitHubProfileResponse;
import com.woowacourse.f12.exception.badrequest.InvalidGitHubLoginException;
import com.woowacourse.f12.support.fixture.MemberFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class GitHubOauthClientTest {

    @Autowired
    @Qualifier("production")
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
        Member corinne = MemberFixture.CORINNE.생성();
        String accessToken = CORINNE_GITHUB.getToken();
        GitHubProfileResponse expected = new GitHubProfileResponse(corinne.getGitHubId(), corinne.getName(),
                corinne.getImageUrl());

        // when
        GitHubProfileResponse gitHubProfileResponse = gitHubOauthClient.getProfile(accessToken);

        // then
        assertThat(gitHubProfileResponse).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void accessToken_요청의_응답_상태_코드가_400대_인_경우_예외를_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> gitHubOauthClient.getAccessToken("wrongCode"))
                .isExactlyInstanceOf(InvalidGitHubLoginException.class);
    }

    @Test
    void 프로필_요청의_응답_상태_코드가_400대_인_경우_예외를_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> gitHubOauthClient.getProfile("wrongToken"))
                .isExactlyInstanceOf(InvalidGitHubLoginException.class);
    }
}
