package com.woowacourse.f12.dto.response.product;

import com.woowacourse.f12.domain.product.Keyboard;
import lombok.Getter;

@Getter
public class KeyboardForReviewResponse {

    private Long id;
    private String name;
    private String imageUrl;

    private KeyboardForReviewResponse() {
    }

    private KeyboardForReviewResponse(final Long id, final String name, final String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static KeyboardForReviewResponse from(final Keyboard keyboard) {
        return new KeyboardForReviewResponse(keyboard.getId(), keyboard.getName(), keyboard.getImageUrl());
    }
}
