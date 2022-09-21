package com.woowacourse.f12.dto.response.product;

import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.presentation.product.CategoryConstant;
import lombok.Getter;

@Getter
public class ProductResponse {

    private Long id;
    private String name;
    private String imageUrl;
    private int reviewCount;
    private double rating;
    private CategoryConstant category;

    private ProductResponse() {
    }

    private ProductResponse(final Long id, final String name, final String imageUrl, final int reviewCount,
                            final double rating, final CategoryConstant category) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.reviewCount = reviewCount;
        this.rating = rating;
        this.category = category;
    }

    public static ProductResponse from(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getReviewCount(),
                product.getAvgRating(),
                CategoryConstant.from(product.getCategory())
        );
    }
}
