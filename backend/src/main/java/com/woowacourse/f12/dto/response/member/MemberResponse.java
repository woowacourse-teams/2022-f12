package com.woowacourse.f12.dto.response.member;

import com.woowacourse.f12.domain.member.Member;
import lombok.Getter;

@Getter
public class MemberResponse {

    private Long id;
    private String gitHubId;
    private String name;
    private String imageUrl;
    private String careerLevel;
    private String jobType;

    private MemberResponse() {
    }

    private MemberResponse(final Long id, final String gitHubId, final String name, final String imageUrl,
                           final String careerLevel, final String jobType) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
    }

    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getGitHubId(), member.getName(), member.getImageUrl(),
                member.getCareerLevel().name(), member.getJobType().name());
    }
}
