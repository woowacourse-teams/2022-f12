package com.woowacourse.f12.presentation.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.exception.badrequest.InvalidCategoryValueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CategoryConstantTest {

    @ParameterizedTest
    @CsvSource(value = {"keyboard:KEYBOARD", "software:SOFTWARE"}, delimiter = ':')
    void 뷰에서_전달된_이름으로_찾아서_반환한다(String viewValue, CategoryConstant expect) {
        // given, when
        CategoryConstant actual = CategoryConstant.findByViewValue(viewValue);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void 뷰에서_잘못된_이름을_전달하면_예외_발생한다() {
        // given
        final String invalidViewName = "invalid";

        // when, then
        assertThatThrownBy(() -> CategoryConstant.findByViewValue(invalidViewName))
                .isExactlyInstanceOf(InvalidCategoryValueException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"KEYBOARD:KEYBOARD", "SOFTWARE:SOFTWARE"}, delimiter = ':')
    void 도메인_열거형으로_찾아서_반환한다(Category category, CategoryConstant expect) {
        // given, when
        CategoryConstant actual = CategoryConstant.fromDomain(category);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void 도메인_열거형으로_찾을_수_없는_경우_예외_발생한다() {
        // given, when, then
        assertThatThrownBy(() -> CategoryConstant.fromDomain(null))
                .isExactlyInstanceOf(InvalidCategoryValueException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"KEYBOARD:KEYBOARD", "SOFTWARE:SOFTWARE"}, delimiter = ':')
    void 도메인_열거형을_찾아서_반환한다(CategoryConstant constant, Category expect) {
        // given, when
        Category actual = constant.toDomain();

        // then
        assertThat(actual).isEqualTo(expect);
    }
}
