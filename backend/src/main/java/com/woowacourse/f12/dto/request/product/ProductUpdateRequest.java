package com.woowacourse.f12.dto.request.product;

import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.presentation.product.CategoryConstant;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductUpdateRequest {

    @NotNull(message = "제품 이름이 없습니다.")
    private String name;

    @NotNull(message = "이미지 주소가 없습니다.")
    private String imageUrl;

    @NotNull(message = "카테고리가 없습니다.")
    private CategoryConstant category;

    private ProductUpdateRequest() {
    }

    public ProductUpdateRequest(final String name, final String imageUrl, final CategoryConstant category) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product toProduct() {
        return Product.builder()
                .category(category.toCategory())
                .name(name)
                .imageUrl(imageUrl)
                .build();
    }
}
