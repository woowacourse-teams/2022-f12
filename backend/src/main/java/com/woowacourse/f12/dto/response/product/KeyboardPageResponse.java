package com.woowacourse.f12.dto.response.product;

import com.woowacourse.f12.domain.product.Keyboard;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class KeyboardPageResponse {

    private boolean hasNext;
    private List<KeyboardResponse> items;

    private KeyboardPageResponse() {
    }

    private KeyboardPageResponse(final boolean hasNext, final List<KeyboardResponse> items) {
        this.hasNext = hasNext;
        this.items = items;
    }

    public static KeyboardPageResponse from(final Slice<Keyboard> slice) {
        final List<KeyboardResponse> keyboards = slice.getContent()
                .stream()
                .map(KeyboardResponse::from)
                .collect(Collectors.toList());
        return new KeyboardPageResponse(slice.hasNext(), keyboards);
    }
}
