package com.woowacourse.f12.dto.response;

import com.woowacourse.f12.domain.Member;
import lombok.Getter;

@Getter
public class LoginMemberResponse {

    private Long id;
    private String gitHubId;
    private String name;
    private String imageUrl;

    private LoginMemberResponse() {
    }

    private LoginMemberResponse(final Long id, final String gitHubId, final String name, final String imageUrl) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static LoginMemberResponse from(final Member member) {
        return new LoginMemberResponse(member.getId(), member.getGitHubId(), member.getName(), member.getImageUrl());
    }
}
