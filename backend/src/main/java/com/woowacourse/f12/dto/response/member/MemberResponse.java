package com.woowacourse.f12.dto.response.member;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.CareerLevelConstant;
import com.woowacourse.f12.dto.JobTypeConstant;
import lombok.Getter;

@Getter
public class MemberResponse {

    private Long id;
    private String gitHubId;
    private String name;
    private String imageUrl;
    private CareerLevelConstant careerLevel;
    private JobTypeConstant jobType;

    private MemberResponse() {
    }

    public MemberResponse(final Long id, final String gitHubId, final String name, final String imageUrl,
                          final CareerLevelConstant careerLevel,
                          final JobTypeConstant jobType) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
    }

    public static MemberResponse from(final Member member) {
        return new MemberResponse(member.getId(), member.getGitHubId(), member.getName(), member.getImageUrl(),
                CareerLevelConstant.findByCareerLevel(member.getCareerLevel()),
                JobTypeConstant.findByJobType(member.getJobType()));
    }
}
