package com.woowacourse.f12.application.product;

import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.woowacourse.f12.domain.product.Keyboard;
import com.woowacourse.f12.domain.product.KeyboardRepository;
import com.woowacourse.f12.dto.response.product.KeyboardPageResponse;
import com.woowacourse.f12.dto.response.product.KeyboardResponse;
import com.woowacourse.f12.exception.notfound.KeyboardNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class KeyboardServiceTest {

    @Mock
    private KeyboardRepository keyboardRepository;

    @InjectMocks
    private KeyboardService keyboardService;

    @Test
    void id_값으로_키보드를_조회한다() {
        // given
        Keyboard keyboard = KEYBOARD_1.생성(1L);

        given(keyboardRepository.findById(anyLong()))
                .willReturn(Optional.of(keyboard));
        // when
        KeyboardResponse keyboardResponse = keyboardService.findById(1L);

        // then
        assertAll(
                () -> verify(keyboardRepository).findById(1L),
                () -> assertThat(keyboardResponse).usingRecursiveComparison()
                        .isEqualTo(KeyboardResponse.from(keyboard))
        );
    }

    @Test
    void 존재하지_않는_id_값으로_키보드를_조회하면_예외를_반환한다() {
        // given
        given(keyboardRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        // when then
        assertAll(
                () -> assertThatThrownBy(() -> keyboardService.findById(1L))
                        .isExactlyInstanceOf(KeyboardNotFoundException.class),
                () -> verify(keyboardRepository).findById(1L)
        );
    }

    @Test
    void 전체_키보드_목록을_조회한다() {
        // given
        Keyboard keyboard = KEYBOARD_1.생성(1L);
        Pageable pageable = PageRequest.of(0, 1);
        given(keyboardRepository.findPageBy(any(Pageable.class)))
                .willReturn(new SliceImpl<>(List.of(keyboard), pageable, false));

        // when
        KeyboardPageResponse keyboardPageResponse = keyboardService.findPage(pageable);

        // then
        assertAll(
                () -> verify(keyboardRepository).findPageBy(any(Pageable.class)),
                () -> assertThat(keyboardPageResponse.isHasNext()).isFalse(),
                () -> assertThat(keyboardPageResponse.getItems()).hasSize(1)
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(KeyboardResponse.from(keyboard))
        );
    }
}
