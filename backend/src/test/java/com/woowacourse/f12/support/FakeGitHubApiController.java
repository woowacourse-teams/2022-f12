package com.woowacourse.f12.support;

import com.woowacourse.f12.dto.request.GitHubTokenRequest;
import com.woowacourse.f12.dto.response.GitHubProfileResponse;
import com.woowacourse.f12.dto.response.GitHubTokenResponse;
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
        return ResponseEntity.ok(new GitHubTokenResponse("accessToken"));
    }

    @GetMapping("/user")
    public ResponseEntity<GitHubProfileResponse> showFakeProfile(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION) final String authorizationHeaderValue) {
        if (authorizationHeaderValue == null) {
            return ResponseEntity.badRequest().build();
        }
        final String[] splitValue = authorizationHeaderValue.split(" ");
        if (splitValue.length != 2 || !splitValue[0].equals("token")) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new GitHubProfileResponse("gitHubId", "name", "url"));
    }
}
