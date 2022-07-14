package com.woowacourse.f12.support;

import com.woowacourse.f12.dto.request.GitHubTokenRequest;
import com.woowacourse.f12.dto.response.GitHubTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<GitHubTokenResponse> createFakeAccessToken(
            @RequestBody final GitHubTokenRequest gitHubTokenRequest) {
        if (!gitHubTokenRequest.getClientId().equals(clientId) || !gitHubTokenRequest.getClientSecret()
                .equals(clientSecret)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new GitHubTokenResponse("accessToken"));
    }
}
