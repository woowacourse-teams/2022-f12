package com.woowacourse.f12.application.product;

import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.exception.badrequest.InvalidPopularProductsSizeException;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PopularProductsCreator {

    private static final int PRODUCTS_MINIMUM_SIZE = 1;

    private final int reviewCount;
    private final double rating;
    private final int maxSize;

    public PopularProductsCreator(@Value("${review-count}") final int reviewCount,
                                  @Value("${rating}") final double rating,
                                  @Value("${products-maximum-size}") final int maxSize) {
        this.reviewCount = reviewCount;
        this.rating = rating;
        this.maxSize = maxSize;
    }

    public List<Product> create(final int size, final BiFunction<Integer, Double, List<Product>> biFunction) {
        checkSize(size);
        final List<Product> products = biFunction.apply(reviewCount, rating);
        Collections.shuffle(products);
        return products.stream()
                .limit(size)
                .collect(Collectors.toList());
    }

    private void checkSize(final int size) {
        if (size < PRODUCTS_MINIMUM_SIZE || size > maxSize) {
            throw new InvalidPopularProductsSizeException(PRODUCTS_MINIMUM_SIZE, maxSize);
        }
    }
}
