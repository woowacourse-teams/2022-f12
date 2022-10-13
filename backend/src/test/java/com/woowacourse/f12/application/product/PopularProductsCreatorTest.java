package com.woowacourse.f12.application.product;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;
import java.util.List;
import org.junit.jupiter.api.Test;

class PopularProductsCreatorTest {

    @Test
    void testMethodNameHere() {
        // given
        Product keyboard = Product.builder()
                .id(1L)
                .name("키보드")
                .imageUrl("이미지 주소")
                .category(Category.KEYBOARD)
                .rating(4.5)
                .reviewCount(10)
                .build();
        Product mouse = Product.builder()
                .id(2L)
                .name("마우스")
                .imageUrl("이미지 주소")
                .category(Category.MOUSE)
                .rating(5)
                .reviewCount(2)
                .build();
        PopularProductsCreator popularProductsCreator = new PopularProductsCreator(2, 4.5);
        FakeProductRepository productRepository = new FakeProductRepository(keyboard, mouse);

        // when
        List<Product> actual = popularProductsCreator.create(2, productRepository::find);

        // then
        assertThat(actual).containsOnly(keyboard, mouse);
    }
}
