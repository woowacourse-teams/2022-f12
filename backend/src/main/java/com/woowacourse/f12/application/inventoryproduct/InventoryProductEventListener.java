package com.woowacourse.f12.application.inventoryproduct;

import com.woowacourse.f12.application.product.ProductDeletedEvent;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryProductEventListener {
    private final InventoryProductRepository inventoryProductRepository;

    public InventoryProductEventListener(final InventoryProductRepository inventoryProductRepository) {
        this.inventoryProductRepository = inventoryProductRepository;
    }

    @EventListener
    public void handle(final ProductDeletedEvent event) {
        inventoryProductRepository.deleteByProductId(event.getProductId());
    }
}
