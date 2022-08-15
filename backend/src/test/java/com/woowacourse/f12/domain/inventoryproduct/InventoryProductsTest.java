package com.woowacourse.f12.domain.inventoryproduct;

import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.exception.badrequest.DuplicatedProfileProductCategoryException;
import com.woowacourse.f12.exception.badrequest.InvalidProfileProductCategoryException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.InventoryProductFixtures.UNSELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InventoryProductsTest {

    @Test
    void 인벤토리에_포함된_대표장비_중_소프트웨어가_있으면_예외를_반환한다() {
        // given
        Product software = SOFTWARE_1.생성();
        List<InventoryProduct> items = List.of(SELECTED_INVENTORY_PRODUCT.생성(null, software));

        // when, then
        assertThatThrownBy(() -> new InventoryProducts(items)).isExactlyInstanceOf(InvalidProfileProductCategoryException.class);
    }

    @Test
    void 인벤토리에_포함된_대표장비_중_중복된_카테고리가_있다면_예외를_반환한다() {
        // given
        Product product1 = KEYBOARD_1.생성();
        Product product2 = KEYBOARD_2.생성();
        List<InventoryProduct> items = List.of(SELECTED_INVENTORY_PRODUCT.생성(null, product1), SELECTED_INVENTORY_PRODUCT.생성(null, product2));

        // when, then
        assertThatThrownBy(() -> new InventoryProducts(items)).isExactlyInstanceOf(DuplicatedProfileProductCategoryException.class);
    }

    @Test
    void 대표장비를_추출한다() {
        // given
        Product product1 = KEYBOARD_1.생성(1L);
        Product product2 = KEYBOARD_2.생성(2L);
        List<InventoryProduct> items = List.of(SELECTED_INVENTORY_PRODUCT.생성(null, product1), UNSELECTED_INVENTORY_PRODUCT.생성(null, product2));
        InventoryProducts inventoryProducts = new InventoryProducts(items);

        // when
        List<Product> profileProducts = inventoryProducts.getProfileProducts();

        // then
        assertThat(profileProducts).hasSize(1)
                .containsExactly(product1);
    }
}
