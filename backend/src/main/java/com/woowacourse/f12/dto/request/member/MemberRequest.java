package com.woowacourse.f12.dto.request.member;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.presentation.member.CareerLevelConstant;
import com.woowacourse.f12.presentation.member.JobTypeConstant;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class MemberRequest {

    @NotNull(message = "직업 경력 내용이 없습니다.")
    private CareerLevelConstant careerLevel;

    @NotNull(message = "직무 내용이 없습니다.")
    private JobTypeConstant jobType;

    private MemberRequest() {
    }

    public MemberRequest(final CareerLevelConstant careerLevel, final JobTypeConstant jobType) {
        this.careerLevel = careerLevel;
        this.jobType = jobType;
    }

    public Member toMember() {
        return Member.builder()
                .careerLevel(careerLevel.toCareerLevel())
                .jobType(jobType.toJobType())
                .build();
    }
}
