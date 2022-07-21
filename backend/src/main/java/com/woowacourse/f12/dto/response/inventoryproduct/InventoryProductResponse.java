package com.woowacourse.f12.dto.response.inventoryproduct;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.dto.response.product.KeyboardResponse;
import lombok.Getter;

@Getter
public class InventoryProductResponse {

    private Long id;
    private boolean selected;
    private KeyboardResponse product;

    private InventoryProductResponse() {
    }

    private InventoryProductResponse(final Long id, final boolean selected, final KeyboardResponse product) {
        this.id = id;
        this.selected = selected;
        this.product = product;
    }

    public static InventoryProductResponse from(final InventoryProduct inventoryProduct) {
        final KeyboardResponse keyboardResponse = KeyboardResponse.from(inventoryProduct.getKeyboard());
        return new InventoryProductResponse(inventoryProduct.getId(), inventoryProduct.isSelected(), keyboardResponse);
    }
}
