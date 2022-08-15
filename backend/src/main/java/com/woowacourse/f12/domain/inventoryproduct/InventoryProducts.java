package com.woowacourse.f12.domain.inventoryproduct;

import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.exception.badrequest.DuplicatedProfileProductCategoryException;
import com.woowacourse.f12.exception.badrequest.InvalidProfileProductCategoryException;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class InventoryProducts {

    private final List<InventoryProduct> items;

    public InventoryProducts(final List<InventoryProduct> items) {
        validateProfileProducts(items);
        this.items = items;
    }

    private void validateProfileProducts(final List<InventoryProduct> items) {
        final List<InventoryProduct> selectedItems = items.stream()
                .filter(InventoryProduct::isSelected)
                .collect(Collectors.toList());
        validateNotContainsSoftware(selectedItems);
        validateCategoryNotDuplicated(selectedItems);
    }

    private void validateNotContainsSoftware(final List<InventoryProduct> selectedItems) {
        final boolean containsSoftware = selectedItems.stream()
                .anyMatch(inventoryProduct -> inventoryProduct.getProduct().isSameCategory(Category.SOFTWARE));
        if (containsSoftware) {
            throw new InvalidProfileProductCategoryException();
        }
    }

    private void validateCategoryNotDuplicated(final List<InventoryProduct> selectedItems) {
        final long distinctCount = selectedItems.stream()
                .map(it -> it.getProduct().getCategory())
                .distinct()
                .count();
        if (distinctCount != selectedItems.size()) {
            throw new DuplicatedProfileProductCategoryException();
        }
    }

    public List<Product> getProfileProducts() {
        return items.stream()
                .filter(InventoryProduct::isSelected)
                .map(InventoryProduct::getProduct)
                .collect(Collectors.toList());
    }
}
