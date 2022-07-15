package com.woowacourse.f12.dto.response;

import com.woowacourse.f12.domain.Member;
import lombok.Getter;

@Getter
public class MemberResponse {

    private Long id;
    private String gitHubId;
    private String name;
    private String imageUrl;

    private MemberResponse() {
    }

    private MemberResponse(final Long id, final String gitHubId, final String name, final String imageUrl) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getGitHubId(), member.getName(), member.getImageUrl());
    }
}
