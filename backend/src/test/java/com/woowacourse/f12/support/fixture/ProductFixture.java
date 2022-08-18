package com.woowacourse.f12.support.fixture;

import static com.woowacourse.f12.domain.product.Category.KEYBOARD;
import static com.woowacourse.f12.domain.product.Category.MONITOR;
import static com.woowacourse.f12.domain.product.Category.MOUSE;
import static com.woowacourse.f12.domain.product.Category.SOFTWARE;
import static com.woowacourse.f12.domain.product.Category.STAND;

import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;

public enum ProductFixture {

    KEYBOARD_1("키보드1", "이미지 주소", KEYBOARD),
    KEYBOARD_2("키보드2", "이미지 주소", KEYBOARD),
    MOUSE_1("마우스1", "이미지 주소", MOUSE),
    MOUSE_2("마우스2", "이미지 주소", MOUSE),
    MONITOR_1("모니터1", "이미지 주소", MONITOR),
    MONITOR_2("모니터2", "이미지 주소", MONITOR),
    STAND_1("거치대1", "이미지 주소", STAND),
    STAND_2("거치대2", "이미지 주소", STAND),
    SOFTWARE_1("소프트웨어1", "이미지 주소", SOFTWARE),
    SOFTWARE_2("소프트웨어2", "이미지 주소", SOFTWARE),
    ;

    private final String name;
    private final String imageUrl;
    private final Category category;

    ProductFixture(final String name, final String imageUrl, final Category category) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product 생성() {
        return 생성(null);
    }

    public Product 생성(final Long id) {
        return Product.builder()
                .id(id)
                .category(category)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .build();
    }
}
