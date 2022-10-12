package com.woowacourse.f12.application.product;

import org.springframework.stereotype.Component;

@Component
public class PopularProductCriteriaDefinitionImpl implements PopularProductCriteriaDefinition {

    private static final int REVIEW_COUNT = 2;
    private static final double RATING = 4.5;
    private static final int POPULAR_PRODUCTS_SIZE = 4;

    @Override
    public int getReviewCount() {
        return REVIEW_COUNT;
    }

    @Override
    public double getRating() {
        return RATING;
    }

    @Override
    public int getPopularProductsSize() {
        return POPULAR_PRODUCTS_SIZE;
    }
}
