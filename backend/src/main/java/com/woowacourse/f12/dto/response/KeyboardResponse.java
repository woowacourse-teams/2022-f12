package com.woowacourse.f12.dto.response;

import com.woowacourse.f12.domain.Keyboard;
import lombok.Getter;

@Getter
public class KeyboardResponse {

    private Long id;
    private String name;

    private KeyboardResponse() {
    }

    private KeyboardResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static KeyboardResponse from(final Keyboard keyboard) {
        return new KeyboardResponse(keyboard.getId(), keyboard.getName());
    }
}
