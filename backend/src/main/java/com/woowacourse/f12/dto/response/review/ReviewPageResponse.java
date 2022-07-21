package com.woowacourse.f12.dto.response.review;

import com.woowacourse.f12.domain.review.Review;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class ReviewPageResponse {

    private final boolean hasNext;
    private final List<ReviewResponse> items;

    private ReviewPageResponse(final boolean hasNext, final List<ReviewResponse> items) {
        this.hasNext = hasNext;
        this.items = items;
    }

    public static ReviewPageResponse from(Slice<Review> slice) {
        List<ReviewResponse> reviews = slice.getContent()
                .stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
        return new ReviewPageResponse(slice.hasNext(), reviews);
    }
}
