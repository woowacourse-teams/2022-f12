package com.woowacourse.f12.dto.request.profile;

import com.woowacourse.f12.presentation.member.CareerLevelConstant;
import com.woowacourse.f12.presentation.member.JobTypeConstant;
import lombok.Getter;

@Getter
public class ProfileSearchRequest {

    private String query;
    private CareerLevelConstant careerLevel;
    private JobTypeConstant jobType;

    private ProfileSearchRequest() {
    }

    public ProfileSearchRequest(final String query, final CareerLevelConstant careerLevel,
                                final JobTypeConstant jobType) {
        this.query = query;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
    }
}
