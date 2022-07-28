package com.woowacourse.f12.domain.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Slice<Product> findPageBy(Pageable pageable);

    Slice<Product> findPageByCategory(Category category, Pageable pageable);
}
