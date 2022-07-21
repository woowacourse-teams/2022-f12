package com.woowacourse.f12.dto.request.member;

import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MemberRequest {

    @NotNull(message = "직업 경력 내용이 없습니다.")
    private CareerLevel careerLevel;

    @NotNull(message = "직무 내용이 없습니다.")
    private JobType jobType;

    private MemberRequest() {
    }

    public MemberRequest(final CareerLevel careerLevel, final JobType jobType) {
        this.careerLevel = careerLevel;
        this.jobType = jobType;
    }
}
