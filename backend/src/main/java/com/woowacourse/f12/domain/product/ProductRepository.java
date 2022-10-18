package com.woowacourse.f12.domain.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    List<Product> findByReviewCountGreaterThanEqualAndRatingGreaterThanEqual(int reviewCount, double rating);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update Product p "
            + "set p.rating = (p.totalRating + :reviewRating) / cast((p.reviewCount + 1) as double), "
            + "p.reviewCount = p.reviewCount + 1, "
            + "p.totalRating = p.totalRating + :reviewRating "
            + "where p.id = :productId")
    void updateProductStatisticsForReviewInsert(Long productId, int reviewRating);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update Product p "
            + "set p.rating = case p.reviewCount when 1 then 0 "
            + "else ((p.totalRating - :reviewRating) / cast((p.reviewCount - 1) as double)) end , "
            + "p.reviewCount = p.reviewCount - 1, "
            + "p.totalRating = p.totalRating - :reviewRating "
            + "where p.id = :productId")
    void updateProductStatisticsForReviewDelete(Long productId, int reviewRating);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update Product p "
            + "set p.rating = (p.totalRating + :ratingGap) / cast(p.reviewCount as double), "
            + "p.totalRating = p.totalRating + :ratingGap "
            + "where p.id = :productId")
    void updateProductStatisticsForReviewUpdate(Long productId, int ratingGap);
}
