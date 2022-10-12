package com.woowacourse.f12.application.product;

import com.woowacourse.f12.domain.product.Product;
import java.util.List;

@FunctionalInterface
public interface FindPopularProductCallback {

    List<Product> find(int reviewCount, double rating);
}
