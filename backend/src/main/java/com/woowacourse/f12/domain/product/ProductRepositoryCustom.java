package com.woowacourse.f12.domain.product;

import java.util.List;

public interface ProductRepositoryCustom {

    List<CareerLevelCount> findCareerLevelCountByProductId(Long productId);

    List<JobTypeCount> findJobTypeCountByProductId(Long productId);
}
