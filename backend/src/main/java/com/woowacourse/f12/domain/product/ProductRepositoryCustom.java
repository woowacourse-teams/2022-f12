package com.woowacourse.f12.domain.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductRepositoryCustom {

    Slice<Product> findBySearchConditions(String keyword, Category category, Pageable pageable);
}
