package com.woowacourse.f12.dto.response.product;

import com.woowacourse.f12.domain.product.Product;
import lombok.Getter;

@Getter
public class KeyboardForReviewResponse {

    private Long id;
    private String name;
    private String imageUrl;

    private KeyboardForReviewResponse() {
    }

    private KeyboardForReviewResponse(final Long id, final String name, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static KeyboardForReviewResponse from(final Product product) {
        return new KeyboardForReviewResponse(product.getId(), product.getName(), product.getImageUrl());
    }
}
