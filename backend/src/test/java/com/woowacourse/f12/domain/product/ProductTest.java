package com.woowacourse.f12.domain.product;

import static com.woowacourse.f12.domain.product.Category.KEYBOARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.review.Review;
import com.woowacourse.f12.exception.internalserver.InvalidReflectReviewException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProductTest {

    @ParameterizedTest
    @CsvSource(value = {"KEYBOARD,true", "MOUSE,false"})
    void 같은_카테고리인지_비교한다(Category category, boolean expected) {
        // given
        Product keyboard = Product.builder()
                .category(KEYBOARD)
                .build();

        // when
        boolean actual = keyboard.isSameCategory(category);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 작성한_리뷰를_제품_정보에_반영한다() {
        // given
        Product product = Product.builder()
                .reviewCount(1)
                .totalRating(5)
                .rating(5)
                .build();

        Review review = Review.builder()
                .content("내용")
                .rating(4)
                .product(product)
                .build();

        // when
        product.reflectReview(review);

        // then
        assertAll(
                () -> assertThat(product.getReviewCount()).isEqualTo(2),
                () -> assertThat(product.getTotalRating()).isEqualTo(9),
                () -> assertThat(product.getRating()).isEqualTo((double) 9 / 2)
        );
    }

    @Test
    void 리뷰_대상이_아닌_제품에_리뷰를_반영하려_하면_예외를_반환한다() {
        // given
        Product product = Product.builder()
                .id(1L)
                .reviewCount(1)
                .totalRating(5)
                .rating(5)
                .build();

        Product notTarget = Product.builder()
                .id(2L)
                .build();

        Review review = Review.builder()
                .content("내용")
                .rating(4)
                .product(notTarget)
                .build();

        // when, then
        assertThatThrownBy(() -> product.reflectReview(review)).isExactlyInstanceOf(
                InvalidReflectReviewException.class);
    }
}
