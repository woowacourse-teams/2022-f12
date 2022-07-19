package com.woowacourse.f12.application;

import com.woowacourse.f12.dto.request.GitHubTokenRequest;
import com.woowacourse.f12.dto.response.GitHubProfileResponse;
import com.woowacourse.f12.dto.response.GitHubTokenResponse;
import com.woowacourse.f12.exception.GitHubServerException;
import com.woowacourse.f12.exception.InvalidGitHubLoginException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GitHubOauthClient {

    private final String clientId;
    private final String secret;
    private final String accessTokenUrl;
    private final String profileUrl;

    public GitHubOauthClient(@Value("${github.client.id}") final String clientId,
                             @Value("${github.client.secret}") final String secret,
                             @Value("${github.url.accessToken}") final String accessTokenUrl,
                             @Value("${github.url.user}") final String profileUrl) {
        this.clientId = clientId;
        this.secret = secret;
        this.accessTokenUrl = accessTokenUrl;
        this.profileUrl = profileUrl;
    }

    public String getAccessToken(final String code) {
        final GitHubTokenRequest gitHubTokenRequest = new GitHubTokenRequest(clientId, secret, code);
        final WebClient webClient = WebClient.builder()
                .baseUrl(accessTokenUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return requestAccessToken(gitHubTokenRequest, webClient).getAccessToken();
    }

    private GitHubTokenResponse requestAccessToken(final GitHubTokenRequest gitHubTokenRequest,
                                                   final WebClient webClient) {
        return webClient.post()
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(gitHubTokenRequest), GitHubTokenRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    throw new InvalidGitHubLoginException();
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    throw new GitHubServerException();
                })
                .bodyToMono(GitHubTokenResponse.class)
                .blockOptional()
                .orElseThrow(InvalidGitHubLoginException::new);
    }

    public GitHubProfileResponse getProfile(final String accessToken) {
        final WebClient webClient = WebClient.builder()
                .baseUrl(profileUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "token " + accessToken)
                .build();
        return requestProfile(webClient);
    }

    private GitHubProfileResponse requestProfile(final WebClient webClient) {
        return webClient.get()
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    throw new InvalidGitHubLoginException();
                })
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {
                    throw new GitHubServerException();
                })
                .bodyToMono(GitHubProfileResponse.class)
                .blockOptional()
                .orElseThrow(InvalidGitHubLoginException::new);
    }
}
