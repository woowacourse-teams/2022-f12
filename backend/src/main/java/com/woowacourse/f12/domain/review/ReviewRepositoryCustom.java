package com.woowacourse.f12.domain.review;

import com.woowacourse.f12.support.CursorPageable;
import com.woowacourse.f12.support.CursorSlice;
import java.util.List;

public interface ReviewRepositoryCustom {

    CursorSlice<Review> findPageBy(CursorPageable cursorPageable);

    List<CareerLevelCount> findCareerLevelCountByProductId(Long productId);

    List<JobTypeCount> findJobTypeCountByProductId(Long productId);
}
