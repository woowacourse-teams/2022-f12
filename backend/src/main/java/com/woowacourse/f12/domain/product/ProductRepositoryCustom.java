package com.woowacourse.f12.domain.product;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductRepositoryCustom {

    Slice<Product> findWithoutSearchConditions(Pageable pageable);

    Slice<Product> findWithSearchConditions(String keyword, Category category, Pageable pageable);

    List<Product> findByReviewCountAndRatingGreaterThanEqual(int reviewCount, double rating);
}
