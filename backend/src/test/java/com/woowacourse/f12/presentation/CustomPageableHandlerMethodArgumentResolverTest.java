package com.woowacourse.f12.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.KeyboardService;
import com.woowacourse.f12.dto.response.KeyboardPageResponse;
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
public class CustomPageableHandlerMethodArgumentResolverTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KeyboardService keyboardService;

    @Test
    void 페이징_실패_최대_페이징_크기_초과() throws Exception {
        // given
        given(keyboardService.findPage(any(Pageable.class)))
                .willReturn(KeyboardPageResponse.from(new SliceImpl<>(List.of())));

        // when
        mockMvc.perform(get("/api/v1/keyboards?page=0&size=151&sort=rating,desc"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        // then
        verify(keyboardService, times(0))
                .findPage(PageRequest.of(0, 151, Sort.by("rating").descending()));
    }
}
