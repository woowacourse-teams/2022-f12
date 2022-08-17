package com.woowacourse.f12.dto.response.review;

import com.woowacourse.f12.domain.member.Member;
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
    private boolean authorMatch;

    private ReviewWithAuthorResponse() {
    }

    private ReviewWithAuthorResponse(final Long id, final ReviewAuthorResponse author, final Long productId,
                                     final String content,
                                     final int rating, final String createdAt, final boolean authorMatch) {
        this.id = id;
        this.author = author;
        this.productId = productId;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
        this.authorMatch = authorMatch;
    }

    public static ReviewWithAuthorResponse of(final Review review, final Long memberId) {
        final Member author = review.getMember();
        final ReviewAuthorResponse authorResponse = ReviewAuthorResponse.from(author);
        return new ReviewWithAuthorResponse(review.getId(), authorResponse, review.getProduct().getId(),
                review.getContent(),
                review.getRating(), review.getCreatedAt().toString(), author.isSameId(memberId));
    }
}
