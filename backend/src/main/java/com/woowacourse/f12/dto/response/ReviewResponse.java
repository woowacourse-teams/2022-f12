package com.woowacourse.f12.dto.response;

import com.woowacourse.f12.domain.Review;
import lombok.Getter;

@Getter
public class ReviewResponse {

    private Long id;
    private ReviewAuthor author;
    private Long productId;
    private String content;
    private int rating;
    private String createdAt;

    private ReviewResponse() {
    }

    private ReviewResponse(final Long id, final ReviewAuthor author, final Long productId, final String content,
                           final int rating, final String createdAt) {
        this.id = id;
        this.author = author;
        this.productId = productId;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public static ReviewResponse from(final Review review) {
        final ReviewAuthor author = ReviewAuthor.from(review.getMember());
        return new ReviewResponse(review.getId(), author, review.getKeyboard().getId(), review.getContent(),
                review.getRating(), review.getCreatedAt().toString());
    }
}
