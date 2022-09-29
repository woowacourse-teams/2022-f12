package com.woowacourse.f12.application.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.dto.request.auth.GitHubTokenRequest;
import com.woowacourse.f12.dto.response.auth.GitHubProfileResponse;
import com.woowacourse.f12.dto.response.auth.GitHubTokenResponse;
import com.woowacourse.f12.exception.badrequest.InvalidGitHubLoginException;
import com.woowacourse.f12.exception.internalserver.GitHubServerException;
import com.woowacourse.f12.exception.internalserver.OauthProcessingException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class GitHubOauthClient {

    private final String clientId;
    private final String secret;
    private final String accessTokenUrl;
    private final String profileUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(Version.HTTP_1_1)
            .followRedirects(Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

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
        final String requestBody = toRequestBody(gitHubTokenRequest);
        try {
            final HttpResponse<String> accessTokenResponse = requestAccessToken(requestBody);
            return parseAccessToken(accessTokenResponse);
        } catch (IOException e) {
            throw new OauthProcessingException();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new OauthProcessingException();
        }
    }

    private String toRequestBody(final GitHubTokenRequest gitHubTokenRequest) {
        try {
            return objectMapper.writeValueAsString(gitHubTokenRequest);
        } catch (JsonProcessingException e) {
            throw new OauthProcessingException();
        }
    }

    private HttpResponse<String> requestAccessToken(final String requestBody)
            throws IOException, InterruptedException {
        final HttpRequest accessTokenRequest = buildAccessTokenRequest(requestBody);
        return httpClient.send(accessTokenRequest, BodyHandlers.ofString());
    }

    private String parseAccessToken(final HttpResponse<String> response) {
        validateStatus(response);
        final GitHubTokenResponse gitHubTokenResponse = parseJson(response, GitHubTokenResponse.class);
        return gitHubTokenResponse.getAccessToken();
    }

    private <T> T parseJson(final HttpResponse<String> response, Class<T> clazz) {
        try {
            return objectMapper.readValue(response.body(), clazz);
        } catch (JsonProcessingException e) {
            throw new OauthProcessingException();
        }
    }

    private void validateStatus(final HttpResponse<String> response) {
        final HttpStatus status = HttpStatus.resolve(response.statusCode());
        if (status == null || status.is4xxClientError()) {
            throw new InvalidGitHubLoginException();
        }
        if (status.is5xxServerError()) {
            throw new GitHubServerException();
        }
    }

    private HttpRequest buildAccessTokenRequest(final String requestBody) {
        return HttpRequest.newBuilder()
                .uri(toURI(accessTokenUrl))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
    }

    private URI toURI(final String accessTokenUrl) {
        try {
            return new URI(accessTokenUrl);
        } catch (URISyntaxException e) {
            throw new OauthProcessingException();
        }
    }

    public GitHubProfileResponse getProfile(final String accessToken) {
        try {
            final HttpRequest profileRequest = buildProfileRequest(accessToken);
            final HttpResponse<String> profileResponse = httpClient.send(profileRequest, BodyHandlers.ofString());
            return parseProfile(profileResponse);
        } catch (IOException | InterruptedException e) {
            throw new OauthProcessingException();
        }
    }

    private HttpRequest buildProfileRequest(final String accessToken) {
        return HttpRequest.newBuilder(toURI(profileUrl))
                .GET()
                .header(HttpHeaders.AUTHORIZATION, "token " + accessToken)
                .build();
    }

    private GitHubProfileResponse parseProfile(final HttpResponse<String> profileResponse) {
        validateStatus(profileResponse);
        return parseJson(profileResponse, GitHubProfileResponse.class);
    }
}
