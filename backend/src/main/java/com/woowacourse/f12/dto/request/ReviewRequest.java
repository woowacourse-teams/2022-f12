package com.woowacourse.f12.dto.request;

import com.woowacourse.f12.domain.Review;
import lombok.Getter;

@Getter
public class ReviewRequest {

    private String content;
    private int rating;

    private ReviewRequest() {
    }

    public ReviewRequest(final String content, final int rating) {
        this.content = content;
        this.rating = rating;
    }

    public Review toReview(final Long productId) {
        return Review.builder()
                .productId(productId)
                .content(this.content)
                .rating(this.rating)
                .build();
    }
}
