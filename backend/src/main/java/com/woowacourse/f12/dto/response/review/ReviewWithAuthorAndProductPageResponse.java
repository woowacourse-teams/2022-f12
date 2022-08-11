package com.woowacourse.f12.dto.response.review;

import com.woowacourse.f12.domain.review.Review;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class ReviewWithAuthorAndProductPageResponse {

    private boolean hasNext;
    private List<ReviewWithAuthorAndProductResponse> items;

    private ReviewWithAuthorAndProductPageResponse() {
    }

    private ReviewWithAuthorAndProductPageResponse(final boolean hasNext,
                                                   final List<ReviewWithAuthorAndProductResponse> items) {
        this.hasNext = hasNext;
        this.items = items;
    }

    public static ReviewWithAuthorAndProductPageResponse from(final Slice<Review> reviews) {
        List<ReviewWithAuthorAndProductResponse> items = reviews.getContent()
                .stream()
                .map(ReviewWithAuthorAndProductResponse::from)
                .collect(Collectors.toList());
        return new ReviewWithAuthorAndProductPageResponse(reviews.hasNext(), items);
    }
}
