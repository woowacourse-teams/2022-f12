package com.woowacourse.f12.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GitHubProfileResponse {

    @JsonProperty("login")
    private String gitHubId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("avatar_url")
    private String imageUrl;

    private GitHubProfileResponse() {
    }

    public GitHubProfileResponse(final String gitHubId, final String name, final String imageUrl) {
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
