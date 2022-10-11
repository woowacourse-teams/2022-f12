package com.woowacourse.f12.domain.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Query(value = "select * from Product where category = :category and review_count >= :reviewCount and avg_rating >= :rating order by rand() limit :rowCount", nativeQuery = true)
    List<Product> findPopularProductsByRandom(String category, int reviewCount, double rating,
                                              int rowCount);
}
