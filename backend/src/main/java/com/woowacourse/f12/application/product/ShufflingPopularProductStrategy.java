package com.woowacourse.f12.application.product;

import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.dto.response.PopularProductsResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ShufflingPopularProductStrategy implements PopularProductStrategy {

    private final PopularProductCriteriaDefinition definition;

    public ShufflingPopularProductStrategy(final PopularProductCriteriaDefinition definition) {
        this.definition = definition;
    }

    @Override
    public final PopularProductsResponse getResult(final FindPopularProductCallback findPopularProductCallback) {
        final List<Product> products = findPopularProductCallback.find(definition.getReviewCount(),
                definition.getRating());
        Collections.shuffle(products);
        final List<Product> parsedProducts = products.stream()
                .limit(definition.getPopularProductsSize())
                .collect(Collectors.toList());
        return PopularProductsResponse.from(parsedProducts);
    }
}
