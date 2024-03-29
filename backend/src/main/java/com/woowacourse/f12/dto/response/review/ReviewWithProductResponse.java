package com.woowacourse.f12.dto.response.review;

import com.woowacourse.f12.domain.review.Review;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import lombok.Getter;

@Getter
public class ReviewWithProductResponse {

    private Long id;
    private ProductResponse product;
    private String content;
    private int rating;
    private String createdAt;

    private ReviewWithProductResponse() {
    }

    private ReviewWithProductResponse(final Long id, final ProductResponse product, final String content,
                                      final int rating, final String createdAt) {
        this.id = id;
        this.product = product;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public static ReviewWithProductResponse from(final Review review) {
        return new ReviewWithProductResponse(review.getId(), ProductResponse.from(review.getProduct()),
                review.getContent(), review.getRating(), review.getCreatedAt().toString());
    }
}
