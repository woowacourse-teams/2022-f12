package com.woowacourse.f12.domain.inventoryproduct;

import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.exception.badrequest.DuplicatedProfileProductCategoryException;
import com.woowacourse.f12.exception.badrequest.InvalidProfileProductCategoryException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.woowacourse.f12.support.fixture.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.fixture.InventoryProductFixtures.UNSELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InventoryProductsTest {

    @Test
    void 인벤토리에_포함된_대표장비_중_소프트웨어가_있으면_예외를_반환한다() {
        // given
        Product software = SOFTWARE_1.생성(1L);
        List<InventoryProduct> items = List.of(SELECTED_INVENTORY_PRODUCT.생성(1L, null, software));

        // when, then
        assertThatThrownBy(() -> new InventoryProducts(items)).isExactlyInstanceOf(InvalidProfileProductCategoryException.class);
    }

    @Test
    void 인벤토리에_포함된_대표장비_중_중복된_카테고리가_있다면_예외를_반환한다() {
        // given
        Product product1 = KEYBOARD_1.생성(1L);
        Product product2 = KEYBOARD_2.생성(2L);
        List<InventoryProduct> items = List.of(SELECTED_INVENTORY_PRODUCT.생성(null, product1), SELECTED_INVENTORY_PRODUCT.생성(null, product2));

        // when, then
        assertThatThrownBy(() -> new InventoryProducts(items)).isExactlyInstanceOf(DuplicatedProfileProductCategoryException.class);
    }

    @Test
    void 대표장비를_추출한다() {
        // given
        InventoryProduct selectedInventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(null, KEYBOARD_1.생성(1L));
        InventoryProduct unselectedInventoryProduct = UNSELECTED_INVENTORY_PRODUCT.생성(null, KEYBOARD_2.생성(2L));
        List<InventoryProduct> items = List.of(selectedInventoryProduct, unselectedInventoryProduct);
        InventoryProducts inventoryProducts = new InventoryProducts(items);

        // when
        List<InventoryProduct> profileProducts = inventoryProducts.getProfileProducts();

        // then
        assertThat(profileProducts).hasSize(1)
                .containsExactly(selectedInventoryProduct);
    }

    @Test
    void 인벤토리_장비에_포함되는지_확인한다() {
        // given
        InventoryProduct selectedInventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, null, KEYBOARD_1.생성(1L));
        InventoryProduct unselectedInventoryProduct = UNSELECTED_INVENTORY_PRODUCT.생성(2L, null, KEYBOARD_2.생성(2L));
        List<InventoryProduct> items = List.of(selectedInventoryProduct, unselectedInventoryProduct);
        InventoryProducts inventoryProducts = new InventoryProducts(items);

        // when
        boolean contains = inventoryProducts.contains(new InventoryProducts(List.of(selectedInventoryProduct, unselectedInventoryProduct)));

        // then
        assertThat(contains).isTrue();
    }

    @Test
    void 인벤토리_장비의_개수를_확인한다() {
        // given
        Product product1 = KEYBOARD_1.생성(1L);
        Product product2 = KEYBOARD_2.생성(2L);
        List<InventoryProduct> items = List.of(SELECTED_INVENTORY_PRODUCT.생성(1L, null, product1), UNSELECTED_INVENTORY_PRODUCT.생성(2L, null, product2));
        InventoryProducts inventoryProducts = new InventoryProducts(items);

        // when
        int size = inventoryProducts.size();

        // then
        assertThat(size).isEqualTo(2);
    }
}
