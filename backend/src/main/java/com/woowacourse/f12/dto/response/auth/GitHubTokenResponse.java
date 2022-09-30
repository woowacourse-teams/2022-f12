package com.woowacourse.f12.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    private GitHubTokenResponse() {
    }

    public GitHubTokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}
