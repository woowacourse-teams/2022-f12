package com.woowacourse.f12.dto.request.member;

import com.woowacourse.f12.presentation.member.CareerLevelConstant;
import com.woowacourse.f12.presentation.member.JobTypeConstant;
import lombok.Getter;

@Getter
public class MemberSearchRequest {

    private String query;
    private CareerLevelConstant careerLevel;
    private JobTypeConstant jobType;

    private MemberSearchRequest() {
    }

    public MemberSearchRequest(final String query, final CareerLevelConstant careerLevel,
                               final JobTypeConstant jobType) {
        this.query = query;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
    }
}
