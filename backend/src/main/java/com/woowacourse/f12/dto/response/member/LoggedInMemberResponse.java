package com.woowacourse.f12.dto.response.member;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.presentation.member.CareerLevelConstant;
import com.woowacourse.f12.presentation.member.JobTypeConstant;
import lombok.Getter;

@Getter
public class LoggedInMemberResponse {

    private Long id;
    private String gitHubId;
    private String name;
    private String imageUrl;
    private CareerLevelConstant careerLevel;
    private JobTypeConstant jobType;
    private int followerCount;

    private LoggedInMemberResponse() {
    }

    private LoggedInMemberResponse(final Long id, final String gitHubId, final String name, final String imageUrl,
                                   final CareerLevelConstant careerLevel, final JobTypeConstant jobType, final int followerCount) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
        this.followerCount = followerCount;
    }

    public static LoggedInMemberResponse from(final Member loggedInMember) {
        return new LoggedInMemberResponse(loggedInMember.getId(), loggedInMember.getGitHubId(), loggedInMember.getName(),
                loggedInMember.getImageUrl(), CareerLevelConstant.from(loggedInMember.getCareerLevel()),
                JobTypeConstant.from(loggedInMember.getJobType()), loggedInMember.getFollowerCount());
    }
}
