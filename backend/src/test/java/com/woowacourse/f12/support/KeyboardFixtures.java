package com.woowacourse.f12.support;

import com.woowacourse.f12.domain.product.Product;

public enum KeyboardFixtures {

    KEYBOARD_1("키보드1", "이미지 주소"),
    KEYBOARD_2("키보드2", "이미지 주소"),
    ;

    private final String name;
    private final String imageUrl;

    KeyboardFixtures(final String name, final String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Product 생성() {
        return 생성(null);
    }

    public Product 생성(final Long id) {
        return Product.builder()
                .id(id)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .build();
    }
}
