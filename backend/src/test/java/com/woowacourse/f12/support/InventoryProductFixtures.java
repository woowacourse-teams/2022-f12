package com.woowacourse.f12.support;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Keyboard;

public enum InventoryProductFixtures {

    SELECTED_INVENTORY_PRODUCT(true),
    UNSELECTED_INVENTORY_PRODUCT(false),
    ;

    private final boolean selected;

    InventoryProductFixtures(final boolean selected) {
        this.selected = selected;
    }

    public InventoryProduct 생성(final Member member, final Keyboard keyboard) {
        return 생성(null, member, keyboard);
    }

    public InventoryProduct 생성(final Long id, final Member member, final Keyboard keyboard) {
        return InventoryProduct.builder()
                .id(id)
                .selected(this.selected)
                .member(member)
                .keyboard(keyboard)
                .build();
    }
}
