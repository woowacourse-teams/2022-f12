package com.woowacourse.f12.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woowacourse.f12.domain.member.Member;
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

    public Member toMember() {
        return Member.builder()
                .gitHubId(this.gitHubId)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .build();
    }
}
