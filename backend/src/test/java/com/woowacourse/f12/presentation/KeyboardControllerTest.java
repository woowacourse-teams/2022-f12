package com.woowacourse.f12.presentation;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.KeyboardService;
import com.woowacourse.f12.domain.Keyboard;
import com.woowacourse.f12.dto.response.KeyboardPageResponse;
import com.woowacourse.f12.dto.response.KeyboardResponse;
import com.woowacourse.f12.exception.KeyboardNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(KeyboardController.class)
class KeyboardControllerTest {

    private static final Keyboard KEYBOARD_1 = Keyboard.builder()
            .id(1L)
            .name("키보드")
            .imageUrl("url")
            .build();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KeyboardService keyboardService;

    @Test
    void 키보드_목록_페이지_조회_성공() throws Exception {
        // given
        given(keyboardService.findPage(any(Pageable.class)))
                .willReturn(KeyboardPageResponse.from(new SliceImpl<>(List.of(KEYBOARD_1))));

        // when
        mockMvc.perform(get("/api/v1/keyboards?page=0&size=150&sort=rating,desc"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(keyboardService).findPage(PageRequest.of(0, 150, Sort.by("rating").descending()));
    }

    @Test
    void 키보드_단일_조회_성공() throws Exception {
        // given
        given(keyboardService.findById(anyLong()))
                .willReturn(KeyboardResponse.from(KEYBOARD_1));

        // when
        mockMvc.perform(get("/api/v1/keyboards/" + KEYBOARD_1.getId()))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(keyboardService).findById(KEYBOARD_1.getId());
    }

    @Test
    void 키보드_단일_조회_실패_존재_하지_않는_아이디() throws Exception {
        // given
        given(keyboardService.findById(anyLong()))
                .willThrow(new KeyboardNotFoundException());

        // when
        mockMvc.perform(get("/api/v1/keyboards/0"))
                .andExpect(status().isNotFound())
                .andDo(print());

        // then
        verify(keyboardService).findById(0L);
    }
}
