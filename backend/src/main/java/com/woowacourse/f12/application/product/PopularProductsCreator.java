package com.woowacourse.f12.application.product;

import com.woowacourse.f12.domain.product.Product;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PopularProductsCreator {

    private final int reviewCount;
    private final double rating;

    public PopularProductsCreator(@Value("${review-count}") final int reviewCount,
                                  @Value("${rating}") final double rating) {
        this.reviewCount = reviewCount;
        this.rating = rating;
    }

    public List<Product> create(final int size, final BiFunction<Integer, Double, List<Product>> biFunction) {
        final List<Product> products = biFunction.apply(reviewCount, rating);
        Collections.shuffle(products);
        return products.stream()
                .limit(size)
                .collect(Collectors.toList());
    }
}
