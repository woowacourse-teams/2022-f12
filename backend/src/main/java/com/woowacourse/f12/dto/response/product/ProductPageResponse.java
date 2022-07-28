package com.woowacourse.f12.dto.response.product;

import com.woowacourse.f12.domain.product.Product;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class ProductPageResponse {

    private boolean hasNext;
    private List<ProductResponse> items;

    private ProductPageResponse() {
    }

    private ProductPageResponse(final boolean hasNext, final List<ProductResponse> items) {
        this.hasNext = hasNext;
        this.items = items;
    }

    public static ProductPageResponse from(final Slice<Product> slice) {
        final List<ProductResponse> productResponses = slice.getContent()
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return new ProductPageResponse(slice.hasNext(), productResponses);
    }
}
