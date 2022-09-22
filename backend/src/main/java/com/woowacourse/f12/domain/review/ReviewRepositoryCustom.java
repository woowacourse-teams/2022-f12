package com.woowacourse.f12.domain.review;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewRepositoryCustom {

    Slice<Review> findPageBy(Pageable pageable);

    List<CareerLevelCount> findCareerLevelCountByProductId(Long productId);

    List<JobTypeCount> findJobTypeCountByProductId(Long productId);
}
