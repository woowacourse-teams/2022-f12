package com.woowacourse.f12.application.product;

import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.fixture.ProductFixture.MOUSE_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.exception.badrequest.InvalidPopularProductsSizeException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PopularProductsCreatorTest {

    private PopularProductsCreator popularProductsCreator;
    private FakeProductRepository productRepository;

    @BeforeEach
    void setUp() {
        this.popularProductsCreator = new PopularProductsCreator(2, 4.5, 100);
    }

    @Test
    void 인기_제품_기준에_따라_인기_제품_목록을_반환한다() {
        // given
        Product keyboard = KEYBOARD_1.생성(1L, 4.5, 10);
        Product mouse = MOUSE_1.생성(2L, 5, 2);
        this.productRepository = new FakeProductRepository(keyboard, mouse);

        // when
        List<Product> actual = popularProductsCreator.create(2, productRepository::find);

        // then
        assertThat(actual).containsOnly(keyboard, mouse);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 101})
    void 인기_제품_개수가_유효하지_않을_경우_예외가_발생한다(int invalidSize) {
        // given
        this.productRepository = new FakeProductRepository();

        // when, then
        assertThatThrownBy(() -> popularProductsCreator.create(invalidSize, productRepository::find))
                .isInstanceOf(InvalidPopularProductsSizeException.class);
    }
}
