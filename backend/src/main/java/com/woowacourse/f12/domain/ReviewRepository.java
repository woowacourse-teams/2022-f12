package com.woowacourse.f12.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Slice<Review> findPageByProductId(Long productId, Pageable pageable);

    Slice<Review> findPageBy(Pageable pageable);
}
