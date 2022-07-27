package com.woowacourse.f12.presentation.product;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.f12.domain.product.Category;
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

    @ParameterizedTest
    @CsvSource(value = {"KEYBOARD:KEYBOARD", "SOFTWARE:SOFTWARE"}, delimiter = ':')
    void 도메인_열거형으로_찾아서_반환한다(Category category, CategoryConstant expect) {
        // given, when
        CategoryConstant actual = CategoryConstant.fromDomain(category);

        // then
        assertThat(actual).isEqualTo(expect);
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
