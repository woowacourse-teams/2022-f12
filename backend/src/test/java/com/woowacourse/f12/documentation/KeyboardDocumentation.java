package com.woowacourse.f12.documentation;

import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_2;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.KeyboardService;
import com.woowacourse.f12.domain.Keyboard;
import com.woowacourse.f12.dto.response.KeyboardPageResponse;
import com.woowacourse.f12.dto.response.KeyboardResponse;
import com.woowacourse.f12.presentation.KeyboardController;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(KeyboardController.class)
class KeyboardDocumentation extends Documentation {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KeyboardService keyboardService;

    @Test
    void 키보드_단일_조회_API_문서화() throws Exception {
        // given
        Keyboard keyboard = KEYBOARD_1.생성(1L);
        given(keyboardService.findById(1L))
                .willReturn(KeyboardResponse.from(keyboard));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/keyboards/1")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("keyboards-get")
                );
    }

    @Test
    void 키보드_목록_조회_API_문서화() throws Exception {
        // given
        Keyboard keyboard1 = KEYBOARD_1.생성(1L);
        Keyboard keyboard2 = KEYBOARD_2.생성(2L);
        PageRequest pageable = PageRequest.of(0, 5, Sort.by("rating").descending());
        SliceImpl<Keyboard> keyboards = new SliceImpl<>(List.of(keyboard1, keyboard2), pageable, false);

        given(keyboardService.findPage(pageable))
                .willReturn(KeyboardPageResponse.from(keyboards));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/keyboards?page=0&size=5&sort=rating,desc")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("keyboards-page-get")
                );
    }
}
