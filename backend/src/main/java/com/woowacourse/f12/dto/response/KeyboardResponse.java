package com.woowacourse.f12.dto.response;

import com.woowacourse.f12.domain.Keyboard;
import lombok.Getter;

@Getter
public class KeyboardResponse {

    private Long id;
    private String name;
    private int reviewCount;
    private double rating;

    private KeyboardResponse() {
    }

    private KeyboardResponse(final Long id, final String name, final int reviewCount, final double rating) {
        this.id = id;
        this.name = name;
        this.reviewCount = reviewCount;
        this.rating = rating;
    }

    public static KeyboardResponse from(final Keyboard keyboard) {
        return new KeyboardResponse(
                keyboard.getId(),
                keyboard.getName(),
                keyboard.getReviewCount(),
                keyboard.getRating()
        );
    }
}
