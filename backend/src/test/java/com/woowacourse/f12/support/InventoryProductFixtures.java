package com.woowacourse.f12.support;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.product.Product;

public enum InventoryProductFixtures {

    SELECTED_INVENTORY_PRODUCT(true),
    UNSELECTED_INVENTORY_PRODUCT(false),
    ;

    private final boolean selected;

    InventoryProductFixtures(final boolean selected) {
        this.selected = selected;
    }

    public InventoryProduct 생성(final Long memberId, final Product product) {
        return 생성(null, memberId, product);
    }

    public InventoryProduct 생성(final Long id, final Long memberId, final Product product) {
        return InventoryProduct.builder()
                .id(id)
                .selected(this.selected)
                .memberId(memberId)
                .product(product)
                .build();
    }
}
