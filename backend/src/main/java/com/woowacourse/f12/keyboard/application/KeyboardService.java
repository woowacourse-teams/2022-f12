package com.woowacourse.f12.keyboard.application;

import com.woowacourse.f12.keyboard.domain.Keyboard;
import com.woowacourse.f12.keyboard.domain.KeyboardRepository;
import com.woowacourse.f12.keyboard.dto.response.KeyboardResponse;
import com.woowacourse.f12.keyboard.exception.KeyboardNotFoundException;
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
}
