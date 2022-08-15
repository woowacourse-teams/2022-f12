package com.woowacourse.f12.domain.product;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.woowacourse.f12.domain.product.Category.KEYBOARD;
import static org.assertj.core.api.Assertions.assertThat;

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
}
