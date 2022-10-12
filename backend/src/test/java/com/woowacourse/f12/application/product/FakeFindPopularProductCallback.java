package com.woowacourse.f12.application.product;

import com.woowacourse.f12.domain.product.Product;
import java.util.List;
import java.util.stream.Collectors;

public class FakeFindPopularProductCallback implements FindPopularProductCallback {

    private final List<Product> products;

    public FakeFindPopularProductCallback(final Product... products) {
        this.products = List.of(products);
    }

    @Override
    public List<Product> find(final int reviewCount, final double rating) {
        return products.stream()
                .filter(it -> it.getReviewCount() >= reviewCount && it.getRating() >= rating)
                .collect(Collectors.toList());
    }
}
