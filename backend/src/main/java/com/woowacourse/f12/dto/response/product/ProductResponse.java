package com.woowacourse.f12.dto.response.product;

import com.woowacourse.f12.domain.product.Keyboard;
import lombok.Getter;

@Getter
public class ProductResponse {

    private Long id;
    private String name;
    private String imageUrl;
    private int reviewCount;
    private double rating;

    private ProductResponse() {
    }

    private ProductResponse(final Long id, final String name, final String imageUrl, final int reviewCount,
                            final double rating) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.reviewCount = reviewCount;
        this.rating = rating;
    }

    public static ProductResponse from(final Keyboard keyboard) {
        return new ProductResponse(
                keyboard.getId(),
                keyboard.getName(),
                keyboard.getImageUrl(),
                keyboard.getReviewCount(),
                keyboard.getRating()
        );
    }
}
