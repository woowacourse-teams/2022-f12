package com.woowacourse.f12.domain.review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ReviewRepositoryCustom {

    Slice<Review> findPageBy(Pageable pageable);
}
