package com.woowacourse.f12.dto.response.product;

import com.woowacourse.f12.domain.product.Product;
import lombok.Getter;

@Getter
public class ProductForReviewResponse {

    private Long id;
    private String name;
    private String imageUrl;

    private ProductForReviewResponse() {
    }

    private ProductForReviewResponse(final Long id, final String name, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static ProductForReviewResponse from(final Product product) {
        return new ProductForReviewResponse(product.getId(), product.getName(), product.getImageUrl());
    }
}
