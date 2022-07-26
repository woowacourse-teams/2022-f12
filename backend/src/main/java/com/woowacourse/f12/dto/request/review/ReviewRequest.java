package com.woowacourse.f12.dto.request.review;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Keyboard;
import com.woowacourse.f12.domain.review.Review;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewRequest {

    @NotNull(message = "리뷰 내용이 없습니다.")
    private String content;

    @NotNull(message = "리뷰 점수가 없습니다.")
    private Integer rating;

    private ReviewRequest() {
    }

    public ReviewRequest(final String content, final Integer rating) {
        this.content = content;
        this.rating = rating;
    }

    public Review toReview(final Keyboard keyboard, final Member member) {
        return Review.builder()
                .keyboard(keyboard)
                .member(member)
                .content(this.content)
                .rating(this.rating)
                .build();
    }
}
