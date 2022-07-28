package com.woowacourse.f12.support;

import static com.woowacourse.f12.support.MemberFixtures.CORINNE;

import com.woowacourse.f12.dto.request.auth.GitHubTokenRequest;
import com.woowacourse.f12.dto.response.auth.GitHubProfileResponse;
import com.woowacourse.f12.dto.response.auth.GitHubTokenResponse;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FakeGitHubApiController {

    private Map<String, String> codeAndToken = Map.of("1", "token1", "2", "token2", CORINNE.생성().getGitHubId(),
            "token3");
    private Map<String, String> tokenAndGitHubId = Map.of("token1", "gitHubId1", "token2", "gitHubId2", "token3",
            CORINNE.생성().getGitHubId());

    private Map<String, String> tokenAndName = Map.of("token1", "name", "token2", "name", "token3",
            CORINNE.생성().getName());

    private Map<String, String> tokenAndImageUrl = Map.of("token1", "url", "token2", "url", "token3",
            CORINNE.생성().getImageUrl());

    private String clientId;
    private String clientSecret;

    public FakeGitHubApiController(@Value("${github.client.id}") final String clientId,
                                   @Value("${github.client.secret}") final String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @PostMapping("/login/oauth/access_token")
    public ResponseEntity<GitHubTokenResponse> issueFakeAccessToken(
            @RequestBody final GitHubTokenRequest gitHubTokenRequest) {
        if (!gitHubTokenRequest.getClientId().equals(clientId) || !gitHubTokenRequest.getClientSecret()
                .equals(clientSecret)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new GitHubTokenResponse(codeAndToken.get(gitHubTokenRequest.getCode())));
    }

    @GetMapping("/user")
    public ResponseEntity<GitHubProfileResponse> showFakeProfile(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) final String authorizationHeaderValue) {
        if (Objects.isNull(authorizationHeaderValue)) {
            return ResponseEntity.badRequest().build();
        }
        final String[] splitValue = authorizationHeaderValue.split(" ");
        if (splitValue.length != 2 || !splitValue[0].equals("token")) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                new GitHubProfileResponse(tokenAndGitHubId.get(splitValue[1]), tokenAndName.get(splitValue[1]),
                        tokenAndImageUrl.get(splitValue[1])));
    }
}
