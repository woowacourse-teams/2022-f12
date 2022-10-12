package com.woowacourse.f12.dto.response;

import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class PopularProductsResponse {

    private List<ProductResponse> items;

    private PopularProductsResponse() {
    }

    private PopularProductsResponse(final List<ProductResponse> items) {
        this.items = items;
    }

    public static PopularProductsResponse from(final List<Product> products) {
        final List<ProductResponse> items = products.stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return new PopularProductsResponse(items);
    }
}
