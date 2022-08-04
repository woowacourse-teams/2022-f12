package com.woowacourse.f12.support;

import com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.dto.request.inventoryproduct.ProfileProductRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;

public enum InventoryProductFixtures {

    SELECTED_INVENTORY_PRODUCT(true),
    UNSELECTED_INVENTORY_PRODUCT(false),
    ;

    private final boolean selected;

    InventoryProductFixtures(final boolean selected) {
        this.selected = selected;
    }

    public InventoryProduct 생성(final Member member, final Product product) {
        return 생성(null, member, product);
    }

    public InventoryProduct 생성(final Long id, final Member member, final Product product) {
        return InventoryProduct.builder()
                .id(id)
                .selected(this.selected)
                .member(member)
                .product(product)
                .build();
    }

    public static ExtractableResponse<Response> 대표_장비_업데이트_한다(final List<Product> selectedProducts,
                                                              final String token) {
        final List<Long> selectedProductIds = selectedProducts.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        return RestAssuredRequestUtil.로그인된_상태로_PATCH_요청을_보낸다("/api/v1/members/inventoryProducts", token,
                new ProfileProductRequest(selectedProductIds));
    }
}
