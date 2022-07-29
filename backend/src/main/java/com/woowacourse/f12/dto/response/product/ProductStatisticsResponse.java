package com.woowacourse.f12.dto.response.product;

import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.dto.CareerLevelConstant;
import com.woowacourse.f12.dto.JobTypeConstant;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class ProductStatisticsResponse {

    private Map<CareerLevelConstant, Double> careerLevel;
    private Map<JobTypeConstant, Double> jobType;

    private ProductStatisticsResponse() {
    }

    private ProductStatisticsResponse(final Map<CareerLevelConstant, Double> careerLevel,
                                      final Map<JobTypeConstant, Double> jobType) {
        this.careerLevel = careerLevel;
        this.jobType = jobType;
    }

    public static ProductStatisticsResponse of(final Map<CareerLevel, Double> enumCareerLevel,
                                               final Map<JobType, Double> enumJobType) {
        final Map<CareerLevelConstant, Double> careerLevel = enumCareerLevel.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> CareerLevelConstant.from(entry.getKey()), Entry::getValue));
        final Map<JobTypeConstant, Double> jobType = enumJobType.entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> JobTypeConstant.from(entry.getKey()), Entry::getValue));
        return new ProductStatisticsResponse(careerLevel, jobType);
    }
}
