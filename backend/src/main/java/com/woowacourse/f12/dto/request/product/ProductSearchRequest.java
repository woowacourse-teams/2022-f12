package com.woowacourse.f12.dto.request.product;

import com.woowacourse.f12.presentation.product.CategoryConstant;
import lombok.Getter;

@Getter
public class ProductSearchRequest {

    private String query;
    private CategoryConstant category;

    private ProductSearchRequest() {
    }

    public ProductSearchRequest(String query, CategoryConstant category) {
        this.query = query;
        this.category = category;
    }
}
