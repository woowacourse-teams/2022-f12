package com.woowacourse.f12.domain.inventoryproduct;

import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.exception.badrequest.DuplicatedProfileProductCategoryException;
import com.woowacourse.f12.exception.badrequest.InvalidProfileProductCategoryException;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Embeddable
public class InventoryProducts {

    @BatchSize(size = 150)
    @OneToMany(mappedBy = "member")
    private List<InventoryProduct> items = new ArrayList<>();

    public InventoryProducts() {
    }

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
                .anyMatch(inventoryProduct -> inventoryProduct.isSameCategory(Category.SOFTWARE));
        if (containsSoftware) {
            throw new InvalidProfileProductCategoryException();
        }
    }

    private void validateCategoryNotDuplicated(final List<InventoryProduct> selectedItems) {
        final long distinctCount = selectedItems.stream()
                .map(InventoryProduct::getProduct)
                .map(Product::getCategory)
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

    public boolean contains(final InventoryProducts inventoryProducts) {
        return new HashSet<>(items).containsAll(inventoryProducts.getItems());
    }

    public int size() {
        return items.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryProducts)) return false;
        InventoryProducts that = (InventoryProducts) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
