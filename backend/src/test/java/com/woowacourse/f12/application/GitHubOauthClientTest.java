package com.woowacourse.f12.application;

import static org.assertj.core.api.Assertions.assertThat;

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
        String accessToken = gitHubOauthClient.getAccessToken("code");

        // then
        assertThat(accessToken).isEqualTo("accessToken");
    }
}
