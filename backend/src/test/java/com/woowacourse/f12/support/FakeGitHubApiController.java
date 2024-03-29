package com.woowacourse.f12.support;

import static com.woowacourse.f12.support.fixture.GitHubProfileFixture.CORINNE_GITHUB;
import static com.woowacourse.f12.support.fixture.GitHubProfileFixture.KLAY_GITHUB;
import static com.woowacourse.f12.support.fixture.GitHubProfileFixture.MINCHO_GITHUB;
import static com.woowacourse.f12.support.fixture.GitHubProfileFixture.OHZZI_GITHUB;
import static com.woowacourse.f12.support.fixture.MemberFixture.ADMIN_KLAY;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.MemberFixture.MINCHO;
import static com.woowacourse.f12.support.fixture.MemberFixture.OHZZI;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.request.auth.GitHubTokenRequest;
import com.woowacourse.f12.dto.response.auth.GitHubProfileResponse;
import com.woowacourse.f12.dto.response.auth.GitHubTokenResponse;
import java.util.Map;
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

    private final Map<String, String> codeAndToken = Map.of(CORINNE_GITHUB.getCode(), CORINNE_GITHUB.getToken(),
            MINCHO_GITHUB.getCode(), MINCHO_GITHUB.getToken(), OHZZI_GITHUB.getCode(), OHZZI_GITHUB.getToken(),
            KLAY_GITHUB.getCode(), KLAY_GITHUB.getToken());

    private final Map<String, Member> tokenAndMember = Map.of(CORINNE_GITHUB.getToken(), CORINNE.생성(),
            MINCHO_GITHUB.getToken(), MINCHO.생성(), OHZZI_GITHUB.getToken(), OHZZI.생성(), KLAY_GITHUB.getToken(),
            ADMIN_KLAY.생성());

    private final String clientId;
    private final String clientSecret;

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
        final String accessToken = codeAndToken.get(gitHubTokenRequest.getCode());
        if (accessToken == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new GitHubTokenResponse(accessToken));
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
        Member member = tokenAndMember.get(splitValue[1]);
        if (member == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(
                new GitHubProfileResponse(member.getGitHubId(), member.getName(), member.getImageUrl()));
    }
}
