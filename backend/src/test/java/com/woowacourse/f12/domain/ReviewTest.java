package com.woowacourse.f12.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.f12.exception.InvalidRatingValueException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReviewTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 5})
    void 리뷰_평점_값이_1과_5_사이_정수면_생성된다(int rating) {
        // given, when
        Review review = Review.builder()
                .rating(rating)
                .build();

        // then
        assertThat(review).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 6})
    void 리뷰_평점_값이_1과_5_사이가_아니면_예외가_발생한다(int rating) {
        // given, when, then
        assertThatThrownBy(
                () -> Review.builder()
                        .rating(rating)
                        .build()
        )
                .isExactlyInstanceOf(InvalidRatingValueException.class);
    }
}
