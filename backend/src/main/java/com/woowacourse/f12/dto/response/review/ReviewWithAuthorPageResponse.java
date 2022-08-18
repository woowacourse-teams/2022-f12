package com.woowacourse.f12.dto.response.review;

import com.woowacourse.f12.domain.review.Review;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class ReviewWithAuthorPageResponse {

    private final boolean hasNext;
    private final List<ReviewWithAuthorResponse> items;

    private ReviewWithAuthorPageResponse(final boolean hasNext, final List<ReviewWithAuthorResponse> items) {
        this.hasNext = hasNext;
        this.items = items;
    }

    public static ReviewWithAuthorPageResponse of(final Slice<Review> slice, final Long memberId) {
        final List<ReviewWithAuthorResponse> reviews = slice.getContent()
                .stream()
                .map(review -> ReviewWithAuthorResponse.of(review, memberId))
                .collect(Collectors.toList());
        return new ReviewWithAuthorPageResponse(slice.hasNext(), reviews);
    }

    public static ReviewWithAuthorPageResponse from(final Slice<Review> slice) {
        final List<ReviewWithAuthorResponse> reviews = slice.getContent()
                .stream()
                .map(ReviewWithAuthorResponse::from)
                .collect(Collectors.toList());
        return new ReviewWithAuthorPageResponse(slice.hasNext(), reviews);
    }
}
