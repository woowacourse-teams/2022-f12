package com.woowacourse.f12.dto.response;

import com.woowacourse.f12.domain.Member;
import lombok.Getter;

@Getter
public class ReviewAuthor {

    private Long id;
    private String gitHubId;
    private String imageUrl;

    private ReviewAuthor() {
    }

    private ReviewAuthor(final Long id, final String gitHubId, final String imageUrl) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.imageUrl = imageUrl;
    }

    public static ReviewAuthor from(Member member) {
        return new ReviewAuthor(member.getId(), member.getGitHubId(), member.getImageUrl());
    }
}
