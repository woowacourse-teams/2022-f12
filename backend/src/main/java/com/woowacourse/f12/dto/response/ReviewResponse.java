package com.woowacourse.f12.dto.response;

import com.woowacourse.f12.domain.Review;
import lombok.Getter;

@Getter
public class ReviewResponse {

    private Long id;
    private Long productId;
    private String content;
    private int rating;

    private ReviewResponse() {
    }

    private ReviewResponse(final Long id, final Long productId, final String content, final int rating) {
        this.id = id;
        this.productId = productId;
        this.content = content;
        this.rating = rating;
    }

    public static ReviewResponse from(final Review review) {
        return new ReviewResponse(review.getId(), review.getProductId(), review.getContent(), review.getRating());
    }
}
