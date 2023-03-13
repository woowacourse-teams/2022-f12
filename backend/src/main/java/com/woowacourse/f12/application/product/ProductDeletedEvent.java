package com.woowacourse.f12.application.product;

import org.springframework.context.ApplicationEvent;

public class ProductDeletedEvent extends ApplicationEvent {

    private final Long productId;

    public ProductDeletedEvent(final Object source, final Long productId) {
        super(source);
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
