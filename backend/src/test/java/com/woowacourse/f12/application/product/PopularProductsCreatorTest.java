package com.woowacourse.f12.application.product;

import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.fixture.ProductFixture.MOUSE_1;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.f12.domain.product.Product;
import java.util.List;
import org.junit.jupiter.api.Test;

class PopularProductsCreatorTest {

    @Test
    void testMethodNameHere() {
        // given
        Product keyboard = KEYBOARD_1.생성(1L, 4.5, 10);
        Product mouse = MOUSE_1.생성(2L, 5, 2);
        PopularProductsCreator popularProductsCreator = new PopularProductsCreator(2, 4.5);
        FakeProductRepository productRepository = new FakeProductRepository(keyboard, mouse);

        // when
        List<Product> actual = popularProductsCreator.create(2, productRepository::find);

        // then
        assertThat(actual).containsOnly(keyboard, mouse);
    }
}
