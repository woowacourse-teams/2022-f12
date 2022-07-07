package com.woowacourse.f12.support;

import com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil;
import com.woowacourse.f12.domain.Review;
import com.woowacourse.f12.dto.request.ReviewRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;

public enum ReviewFixtures {

    REVIEW_RATING_1("리뷰 내용", 1),
    REVIEW_RATING_2("리뷰 내용", 2),
    REVIEW_RATING_3("리뷰 내용", 3),
    REVIEW_RATING_4("리뷰 내용", 4),
    REVIEW_RATING_5("리뷰 내용", 5),
    ;

    private final String content;
    private final int rating;

    ReviewFixtures(final String content, final int rating) {
        this.content = content;
        this.rating = rating;
    }

    public Review 작성(Long productId) {
        return 작성(null, productId);
    }

    public Review 작성(Long reviewId, Long productId) {
        return Review.builder()
                .id(reviewId)
                .productId(productId)
                .content(this.content)
                .rating(this.rating)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public ExtractableResponse<Response> 작성_요청을_보낸다(Long productId) {
        final ReviewRequest reviewRequest = new ReviewRequest(this.content, this.rating);
        return RestAssuredRequestUtil.POST_요청을_보낸다("/api/v1/keyboards/" + productId + "/reviews", reviewRequest);
    }
}
