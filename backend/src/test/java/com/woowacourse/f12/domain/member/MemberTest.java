package com.woowacourse.f12.domain.member;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void getProfileProducts() {
        // given
        Product keyboard = Product.builder()
                .category(Category.KEYBOARD)
                .imageUrl("keyboardImage")
                .name("keyboard")
                .build();
        Product mouse = Product.builder()
                .category(Category.MOUSE)
                .imageUrl("mouseImage")
                .name("mouse")
                .build();
        InventoryProduct selectedInventoryProduct = InventoryProduct.builder()
                .product(keyboard)
                .selected(true)
                .build();
        InventoryProduct unselectedInventoryProduct = InventoryProduct.builder()
                .product(mouse)
                .selected(false)
                .build();
        Member member = Member.builder()
                .careerLevel(CareerLevel.JUNIOR)
                .jobType(JobType.BACKEND)
                .gitHubId("githubId")
                .imageUrl("imageUrl")
                .name("klay")
                .inventoryProducts(List.of(selectedInventoryProduct, unselectedInventoryProduct))
                .build();

        // when
        List<Product> profileProducts = member.getProfileProducts();

        // then
        assertThat(profileProducts)
                .containsOnly(selectedInventoryProduct.getProduct());
    }

    @Test
    void Builder_테스트() {
        Member member = Member.builder()
                .build();

        assertThat(member.getInventoryProducts()).isInstanceOf(ArrayList.class);
    }
}
