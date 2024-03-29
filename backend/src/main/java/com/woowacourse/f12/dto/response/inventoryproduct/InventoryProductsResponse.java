package com.woowacourse.f12.dto.response.inventoryproduct;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class InventoryProductsResponse {

    private List<InventoryProductResponse> items;

    private InventoryProductsResponse() {
    }

    private InventoryProductsResponse(final List<InventoryProductResponse> items) {
        this.items = items;
    }

    public static InventoryProductsResponse from(final List<InventoryProduct> inventoryProducts) {
        return new InventoryProductsResponse(inventoryProducts.stream()
                .map(InventoryProductResponse::from)
                .collect(Collectors.toList()));
    }
}
