package com.woowacourse.f12.dto.response;

import com.woowacourse.f12.domain.Review;
import lombok.Getter;

@Getter
public class ReviewWithProductResponse {

    private Long id;
    private ReviewAuthorResponse author;
    private KeyboardForReviewResponse product;
    private String content;
    private int rating;
    private String createdAt;

    private ReviewWithProductResponse() {
    }

    private ReviewWithProductResponse(final Long id, final ReviewAuthorResponse author,
                                      final KeyboardForReviewResponse product,
                                      final String content, final int rating, final String createdAt) {
        this.id = id;
        this.author = author;
        this.product = product;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public static ReviewWithProductResponse from(final Review review) {
        final KeyboardForReviewResponse product = KeyboardForReviewResponse.from(review.getKeyboard());
        final ReviewAuthorResponse author = ReviewAuthorResponse.from(review.getMember());
        return new ReviewWithProductResponse(review.getId(), author, product, review.getContent(), review.getRating(),
                review.getCreatedAt().toString());
    }
}
