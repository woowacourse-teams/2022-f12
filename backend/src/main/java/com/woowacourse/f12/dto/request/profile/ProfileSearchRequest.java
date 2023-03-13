package com.woowacourse.f12.dto.request.profile;

import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.presentation.member.CareerLevelConstant;
import com.woowacourse.f12.presentation.member.JobTypeConstant;
import javax.annotation.Nullable;
import lombok.Getter;

@Getter
public class ProfileSearchRequest {

    private String query;
    @Nullable
    private CareerLevelConstant careerLevel;
    @Nullable
    private JobTypeConstant jobType;

    private ProfileSearchRequest() {
    }

    public ProfileSearchRequest(final String query, @Nullable final CareerLevelConstant careerLevel,
                                @Nullable final JobTypeConstant jobType) {
        this.query = query;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
    }

    @Nullable
    public CareerLevel parseCareerLevel() {
        if (careerLevel == null) {
            return null;
        }
        return careerLevel.toCareerLevel();
    }

    @Nullable
    public JobType parseJobType() {
        if (jobType == null) {
            return null;
        }
        return jobType.toJobType();
    }
}
