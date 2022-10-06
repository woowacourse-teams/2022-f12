package com.woowacourse.f12.config;

import com.woowacourse.f12.application.auth.GitHubOauthClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OauthClientConfig {

    @Bean
    @Qualifier("production")
    public GitHubOauthClient productionGitHubOauthClient(@Value("${github.client.id}") final String clientId,
                                                         @Value("${github.client.secret}") final String secret,
                                                         @Value("${github.url.accessToken}") final String accessTokenUrl,
                                                         @Value("${github.url.user}") final String profileUrl) {
        return new GitHubOauthClient(clientId, secret, accessTokenUrl, profileUrl);
    }

    @Bean
    @Qualifier("admin")
    public GitHubOauthClient adminGitHubOauthClient(@Value("${github.client.admin-id}") final String clientId,
                                                    @Value("${github.client.admin-secret}") final String secret,
                                                    @Value("${github.url.accessToken}") final String accessTokenUrl,
                                                    @Value("${github.url.user}") final String profileUrl) {
        return new GitHubOauthClient(clientId, secret, accessTokenUrl, profileUrl);
    }
}
