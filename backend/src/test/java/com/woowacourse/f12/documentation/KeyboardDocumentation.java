package com.woowacourse.f12.documentation;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.KeyboardService;
import com.woowacourse.f12.domain.Keyboard;
import com.woowacourse.f12.dto.response.KeyboardResponse;
import com.woowacourse.f12.presentation.KeyboardController;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
        Keyboard keyboard = Keyboard.builder()
                .id(1L)
                .name("키보드 이름")
                .imageUrl("이미지 url")
                .build();
        BDDMockito.given(keyboardService.findById(1L))
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
}
