package com.woowacourse.f12.domain.review;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.MID_LEVEL;
import static com.woowacourse.f12.domain.member.CareerLevel.NONE;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.ETC;
import static com.woowacourse.f12.domain.member.JobType.FRONTEND;
import static com.woowacourse.f12.domain.member.JobType.MOBILE;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MemberInfoStatisticsTest {

    @Test
    void 연차에_대한_비율을_계산하여_반환한다() {
        // given
        List<CareerLevelCount> careerLevelCounts = List.of(new CareerLevelCount(JUNIOR, 2),
                new CareerLevelCount(SENIOR, 2));
        MemberInfoStatistics<CareerLevelCount, CareerLevel> memberInfoStatistics = new MemberInfoStatistics<>(
                careerLevelCounts);

        // when
        Map<CareerLevel, Double> result = memberInfoStatistics.calculateStatistics(CareerLevel.values());

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(Map.of(JUNIOR, 0.50, SENIOR, 0.50, NONE, 0.00, MID_LEVEL, 0.00));
    }

    @Test
    void 직군에_대한_비율을_계산하여_반환한다() {
        // given
        List<JobTypeCount> jobTypeCounts = List.of(new JobTypeCount(BACKEND, 1), new JobTypeCount(FRONTEND, 1),
                new JobTypeCount(MOBILE, 1));
        MemberInfoStatistics<JobTypeCount, JobType> memberInfoStatistics = new MemberInfoStatistics<>(jobTypeCounts);

        // when
        Map<JobType, Double> result = memberInfoStatistics.calculateStatistics(JobType.values());

        // then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(Map.of(BACKEND, 0.33, FRONTEND, 0.33, MOBILE, 0.33, ETC, 0.0));
    }
}
