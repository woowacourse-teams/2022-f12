package com.woowacourse.f12.documentation;

import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_2;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_4;
import static com.woowacourse.f12.support.ReviewFixtures.REVIEW_RATING_5;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.JwtProvider;
import com.woowacourse.f12.application.ReviewService;
import com.woowacourse.f12.domain.Keyboard;
import com.woowacourse.f12.dto.request.ReviewRequest;
import com.woowacourse.f12.dto.response.ReviewPageResponse;
import com.woowacourse.f12.dto.response.ReviewWithProductPageResponse;
import com.woowacourse.f12.presentation.ReviewController;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ReviewController.class)
public class ReviewDocumentation extends Documentation {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private JwtProvider jwtProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 리뷰_작성_API_문서화() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(reviewService.save(anyLong(), any(ReviewRequest.class)))
                .willReturn(1L);
        ReviewRequest reviewRequest = new ReviewRequest("content", 5);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/keyboards/" + 1L + "/reviews")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
        );

        // then
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andDo(
                        document("reviews-create")
                );
    }

    @Test
    void 특정_제품의_리뷰_목록_조회_API_문서화() throws Exception {
        // given
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Keyboard keyboard = KEYBOARD_1.생성(1L);
        ReviewPageResponse reviewPageResponse = ReviewPageResponse.from(
                new SliceImpl<>(List.of(REVIEW_RATING_5.작성(1L, keyboard), REVIEW_RATING_4.작성(2L, keyboard)), pageable, false));

        given(reviewService.findPageByProductId(anyLong(), any(Pageable.class)))
                .willReturn(reviewPageResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
                        get("/api/v1/keyboards/1/reviews?page=0&size=10&sort=createdAt,desc"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("reviews-by-product-page-get")
                );
    }

    @Test
    void 모든_리뷰_목록_페이지_조회_API_문서화() throws Exception {
        // given
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Keyboard keyboard1 = KEYBOARD_1.생성(1L);
        Keyboard keyboard2 = KEYBOARD_2.생성(2L);
        ReviewWithProductPageResponse reviewWithProductPageResponse = ReviewWithProductPageResponse.from(
                new SliceImpl<>(List.of(REVIEW_RATING_5.작성(1L, keyboard1), REVIEW_RATING_4.작성(2L, keyboard2)), pageable, false));

        given(reviewService.findPage(any(Pageable.class)))
                .willReturn(reviewWithProductPageResponse);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/reviews?page=0&size=10&sort=createdAt,desc"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("reviews-page-get")
                );
    }
}
