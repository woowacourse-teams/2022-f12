package com.woowacourse.f12.domain.review;

import com.woowacourse.f12.support.CursorSlice;
import java.util.List;

public interface ReviewRepositoryCustom {

    CursorSlice<Review> findRecentPageBy(Long cursor, Integer size);

    List<CareerLevelCount> findCareerLevelCountByProductId(Long productId);

    List<JobTypeCount> findJobTypeCountByProductId(Long productId);
}
