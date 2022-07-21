package com.woowacourse.f12.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.f12.dto.response.GitHubProfileResponse;
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
        String accessToken = gitHubOauthClient.getAccessToken("1");

        // then
        assertThat(accessToken).isEqualTo("token1");
    }

    @Test
    void GitHub에_프로필을_요청한다() {
        // given
        String accessToken = "token1";
        GitHubProfileResponse expected = new GitHubProfileResponse("gitHubId1", "name", "url");

        // when
        GitHubProfileResponse gitHubProfileResponse = gitHubOauthClient.getProfile(accessToken);

        // then
        assertThat(gitHubProfileResponse).usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
