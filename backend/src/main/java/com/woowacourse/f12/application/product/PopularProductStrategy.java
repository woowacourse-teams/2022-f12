package com.woowacourse.f12.application.product;

import com.woowacourse.f12.dto.response.PopularProductsResponse;

public interface PopularProductStrategy {

    PopularProductsResponse getResult(final FindPopularProductCallback findPopularProductCallback);
}
