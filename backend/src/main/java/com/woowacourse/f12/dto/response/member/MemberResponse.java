package com.woowacourse.f12.dto.response.member;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.presentation.member.CareerLevelConstant;
import com.woowacourse.f12.presentation.member.JobTypeConstant;
import lombok.Getter;

@Getter
public class MemberResponse {

    private Long id;
    private String gitHubId;
    private String name;
    private String imageUrl;
    private CareerLevelConstant careerLevel;
    private JobTypeConstant jobType;
    private int followerCount;
    private boolean following;

    private MemberResponse() {
    }

    private MemberResponse(final Long id, final String gitHubId, final String name, final String imageUrl,
                           final CareerLevelConstant careerLevel, final JobTypeConstant jobType, final int followerCount,
                           final boolean following) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
        this.followerCount = followerCount;
        this.following = following;
    }

    public static MemberResponse from(final Member member, final boolean following) {
        return new MemberResponse(member.getId(), member.getGitHubId(), member.getName(), member.getImageUrl(),
                CareerLevelConstant.from(member.getCareerLevel()), JobTypeConstant.from(member.getJobType()),
                member.getFollowerCount(), following);
    }
}
