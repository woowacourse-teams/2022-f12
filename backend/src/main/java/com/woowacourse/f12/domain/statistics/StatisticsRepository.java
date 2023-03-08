package com.woowacourse.f12.domain.statistics;

import java.util.List;

public interface StatisticsRepository {
    List<CareerLevelCount> findCareerLevelCountByProductId(Long productId);

    List<JobTypeCount> findJobTypeCountByProductId(Long productId);
}
