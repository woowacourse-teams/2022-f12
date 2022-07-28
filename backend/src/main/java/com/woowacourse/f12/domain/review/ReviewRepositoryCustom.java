package com.woowacourse.f12.domain.review;

import java.util.List;

public interface ReviewRepositoryCustom {

    List<CareerLevelCount> findCareerLevelCountByProductId(Long productId);

    List<JobTypeCount> findJobTypeCountByProductId(Long productId);
}
