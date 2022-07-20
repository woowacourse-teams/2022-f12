package com.woowacourse.f12.dto.response;

import com.woowacourse.f12.domain.Member;
import lombok.Getter;

@Getter
public class ReviewAuthorResponse {

    private Long id;
    private String gitHubId;
    private String imageUrl;

    private ReviewAuthorResponse() {
    }

    private ReviewAuthorResponse(final Long id, final String gitHubId, final String imageUrl) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.imageUrl = imageUrl;
    }

    public static ReviewAuthorResponse from(Member member) {
        return new ReviewAuthorResponse(member.getId(), member.getGitHubId(), member.getImageUrl());
    }
}
