package com.woowacourse.f12.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.exception.BlankContentException;
import com.woowacourse.f12.exception.InvalidContentLengthException;
import com.woowacourse.f12.exception.InvalidRatingValueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReviewTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 5})
    void 리뷰_평점_값이_1과_5_사이_정수면_생성된다(int rating) {
        // given, when
        Review review = Review.builder()
                .rating(rating)
                .content("내용")
                .build();

        // then
        assertThat(review).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 6})
    void 리뷰_평점_값이_1과_5_사이가_아니면_예외가_발생한다(int rating) {
        // given, when, then
        assertThatThrownBy(() -> Review.builder()
                .rating(rating)
                .content("내용")
                .build())
                .isExactlyInstanceOf(InvalidRatingValueException.class);
    }

    @Test
    void 리뷰_내용의_길이가_1000자_이하면_생성된다() {
        // given
        String invalidContent = "1".repeat(1000);

        // when
        Review review = Review.builder()
                .rating(5)
                .content(invalidContent)
                .build();

        // then
        assertThat(review).isNotNull();
    }

    @Test
    void 리뷰_내용의_길이가_1000자가_넘으면_예외가_발생한다() {
        // given
        String invalidContent = "1".repeat(1001);

        // when, then
        assertThatThrownBy(() -> Review.builder()
                .rating(5)
                .content(invalidContent)
                .build())
                .isExactlyInstanceOf(InvalidContentLengthException.class);
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = " ")
    void 리뷰_내용이_EMPTY_BLANK(String invalidContent) {
        // given, when, then
        assertThatThrownBy(() -> Review.builder()
                .rating(5)
                .content(invalidContent)
                .build())
                .isExactlyInstanceOf(BlankContentException.class);
    }

    @ParameterizedTest
    @CsvSource({"1, true", "2, false"})
    void 리뷰의_작성자인지_반환한다(Long targetMemberId, boolean expected) {
        // given
        Member author = Member.builder()
                .id(1L)
                .build();
        Member targetMember = Member.builder()
                .id(targetMemberId)
                .build();
        Review review = Review.builder()
                .member(author)
                .content("내용")
                .rating(5)
                .build();

        // when
        boolean actual = review.isWrittenBy(targetMember);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 리뷰를_수정한다() {
        // given
        Review review = Review.builder()
                .id(1L)
                .content("내용")
                .rating(5)
                .build();
        Review updateReview = Review.builder()
                .id(2L)
                .content("수정한 내용")
                .rating(4)
                .build();

        // when
        review.update(updateReview);

        // then
        assertAll(
                () -> assertThat(review.getId()).isEqualTo(1L),
                () -> assertThat(review).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(updateReview)
        );
    }
}
