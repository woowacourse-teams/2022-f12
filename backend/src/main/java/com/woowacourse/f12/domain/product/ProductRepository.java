package com.woowacourse.f12.domain.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    List<Product> findByReviewCountAndRatingGreaterThanEqual(int reviewCount, double rating);
}
