package com.woowacourse.f12.support;

import com.woowacourse.f12.domain.Member;
import com.woowacourse.f12.dto.response.GitHubProfileResponse;

public enum MemberFixtures {

    CORINNE("hamcheeseburger", "유현지", "corinne_url"),
    CORINNE_UPDATED("hamcheeseburger", "괴물개발자", "corinne_url");

    private final String gitHubId;
    private final String name;
    private final String imageUrl;

    MemberFixtures(final String gitHubId, final String name, final String imageUrl) {
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Member 생성() {
        return 생성(null);
    }

    public Member 생성(final Long id) {
        return Member.builder()
                .id(id)
                .gitHubId(this.gitHubId)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .build();
    }

    public GitHubProfileResponse 깃허브_프로필() {
        return new GitHubProfileResponse(this.gitHubId, this.name, this.imageUrl);
    }
}
