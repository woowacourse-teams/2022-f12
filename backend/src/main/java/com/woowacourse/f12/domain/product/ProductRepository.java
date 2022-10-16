package com.woowacourse.f12.domain.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    List<Product> findByReviewCountGreaterThanEqualAndRatingGreaterThanEqual(int reviewCount, double rating);
}
