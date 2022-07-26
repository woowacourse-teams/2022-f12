package com.woowacourse.f12.dto.response.inventoryproduct;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import lombok.Getter;

@Getter
public class InventoryProductResponse {

    private Long id;
    private boolean selected;
    private ProductResponse product;

    private InventoryProductResponse() {
    }

    private InventoryProductResponse(final Long id, final boolean selected, final ProductResponse product) {
        this.id = id;
        this.selected = selected;
        this.product = product;
    }

    public static InventoryProductResponse from(final InventoryProduct inventoryProduct) {
        final ProductResponse productResponse = ProductResponse.from(inventoryProduct.getKeyboard());
        return new InventoryProductResponse(inventoryProduct.getId(), inventoryProduct.isSelected(), productResponse);
    }
}
