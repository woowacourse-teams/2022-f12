package com.woowacourse.f12.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GitHubTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    private GitHubTokenResponse() {
    }

    public GitHubTokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
