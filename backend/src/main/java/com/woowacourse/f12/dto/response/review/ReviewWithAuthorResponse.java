package com.woowacourse.f12.dto.response.review;

import com.woowacourse.f12.domain.review.Review;
import lombok.Getter;

@Getter
public class ReviewWithAuthorResponse {

    private Long id;
    private ReviewAuthorResponse author;
    private Long productId;
    private String content;
    private int rating;
    private String createdAt;

    private ReviewWithAuthorResponse() {
    }

    private ReviewWithAuthorResponse(final Long id, final ReviewAuthorResponse author, final Long productId,
                                     final String content,
                                     final int rating, final String createdAt) {
        this.id = id;
        this.author = author;
        this.productId = productId;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public static ReviewWithAuthorResponse from(final Review review) {
        final ReviewAuthorResponse author = ReviewAuthorResponse.from(review.getMember());
        return new ReviewWithAuthorResponse(review.getId(), author, review.getProduct().getId(), review.getContent(),
                review.getRating(), review.getCreatedAt().toString());
    }
}
