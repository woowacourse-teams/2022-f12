package com.woowacourse.f12.application.product;

import com.woowacourse.f12.domain.product.Keyboard;
import com.woowacourse.f12.domain.product.KeyboardRepository;
import com.woowacourse.f12.dto.response.product.KeyboardPageResponse;
import com.woowacourse.f12.dto.response.product.KeyboardResponse;
import com.woowacourse.f12.exception.notfound.KeyboardNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class KeyboardService {

    private final KeyboardRepository keyboardRepository;

    public KeyboardService(final KeyboardRepository keyboardRepository) {
        this.keyboardRepository = keyboardRepository;
    }

    public KeyboardResponse findById(final Long id) {
        final Keyboard keyboard = keyboardRepository.findById(id)
                .orElseThrow(KeyboardNotFoundException::new);
        return KeyboardResponse.from(keyboard);
    }

    public KeyboardPageResponse findPage(final Pageable pageable) {
        return KeyboardPageResponse.from(keyboardRepository.findPageBy(pageable));
    }
}
