package com.woowacourse.f12.dto.response.review;

import com.woowacourse.f12.domain.review.Review;
import lombok.Getter;

@Getter
public class ReviewResponse {

    private Long id;
    private ReviewAuthorResponse author;
    private Long productId;
    private String content;
    private int rating;
    private String createdAt;

    private ReviewResponse() {
    }

    private ReviewResponse(final Long id, final ReviewAuthorResponse author, final Long productId, final String content,
                           final int rating, final String createdAt) {
        this.id = id;
        this.author = author;
        this.productId = productId;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public static ReviewResponse from(final Review review) {
        final ReviewAuthorResponse author = ReviewAuthorResponse.from(review.getMember());
        return new ReviewResponse(review.getId(), author, review.getKeyboard().getId(), review.getContent(),
                review.getRating(), review.getCreatedAt().toString());
    }
}
