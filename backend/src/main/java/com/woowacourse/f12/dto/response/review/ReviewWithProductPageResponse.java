package com.woowacourse.f12.dto.response.review;

import com.woowacourse.f12.domain.review.Review;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class ReviewWithProductPageResponse {

    private boolean hasNext;
    private List<ReviewWithProductResponse> items;

    private ReviewWithProductPageResponse() {
    }

    private ReviewWithProductPageResponse(final boolean hasNext, final List<ReviewWithProductResponse> items) {
        this.hasNext = hasNext;
        this.items = items;
    }

    public static ReviewWithProductPageResponse from(final Slice<Review> page) {
        final List<ReviewWithProductResponse> items = page.getContent().stream()
                .map(ReviewWithProductResponse::from)
                .collect(Collectors.toList());
        return new ReviewWithProductPageResponse(page.hasNext(), items);
    }
}
