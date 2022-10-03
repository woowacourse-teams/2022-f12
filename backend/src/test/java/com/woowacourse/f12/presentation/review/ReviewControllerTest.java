package com.woowacourse.f12.presentation.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.auth.token.JwtProvider;
import com.woowacourse.f12.application.auth.token.MemberPayload;
import com.woowacourse.f12.application.review.ReviewService;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.Role;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.dto.request.review.ReviewRequest;
import com.woowacourse.f12.dto.response.review.ReviewWithAuthorAndProductPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithAuthorPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductPageResponse;
import com.woowacourse.f12.dto.response.review.ReviewWithProductResponse;
import com.woowacourse.f12.exception.badrequest.*;
import com.woowacourse.f12.exception.forbidden.NotAuthorException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import com.woowacourse.f12.exception.notfound.ProductNotFoundException;
import com.woowacourse.f12.exception.notfound.ReviewNotFoundException;
import com.woowacourse.f12.presentation.PresentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.MemberFixture.MINCHO;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_2;
import static com.woowacourse.f12.support.fixture.ReviewFixture.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest extends PresentationTest {

    private static final long PRODUCT_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private JwtProvider jwtProvider;

    public ReviewControllerTest() {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void 리뷰_생성_성공() throws Exception {
        // given
        ReviewRequest reviewRequest = new ReviewRequest("content", 5);
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willReturn(1L);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/products/" + PRODUCT_ID + "/reviews")
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

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(PRODUCT_ID), eq(1L),
                        any(ReviewRequest.class))
        );
    }

    @Test
    void 리뷰_생성_실패_Request_DTO_의_필드가_null_인_경우() throws Exception {
        // given
        ReviewRequest reviewRequest = new ReviewRequest(null, null);
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new BlankContentException());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/products/" + PRODUCT_ID + "/reviews")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(reviewService, times(0)).saveReviewAndInventoryProduct(eq(PRODUCT_ID), eq(1L),
                        any(ReviewRequest.class))
        );
    }

    @Test
    void 리뷰_생성_실패_rating_타입이_올바르지_않을_경우() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Map<String, Object> reviewRequest = new HashMap<>();
        reviewRequest.put("content", "내용");
        reviewRequest.put("rating", "문자");
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new BlankContentException());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/products/" + PRODUCT_ID + "/reviews")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(reviewService, times(0)).saveReviewAndInventoryProduct(eq(PRODUCT_ID), eq(1L),
                        any(ReviewRequest.class))
        );
    }

    @Test
    void 리뷰_생성_실패_리뷰_내용이_존재하지_않음() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        ReviewRequest reviewRequest = new ReviewRequest("", 5);
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new BlankContentException());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/products/" + PRODUCT_ID + "/reviews")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(PRODUCT_ID), eq(1L),
                        any(ReviewRequest.class))
        );
    }

    @Test
    void 리뷰_생성_실패_리뷰_내용_최대_길이_초과() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        String content = "a".repeat(1001);
        ReviewRequest reviewRequest = new ReviewRequest(content, 5);
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new InvalidContentLengthException(1000));

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/products/" + PRODUCT_ID + "/reviews")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(PRODUCT_ID), eq(1L),
                        any(ReviewRequest.class))
        );
    }

    @Test
    void 리뷰_생성_실패_평점이_1부터_5사이_정수가_아님() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        ReviewRequest reviewRequest = new ReviewRequest("내용", 0);
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new InvalidRatingValueException());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/products/" + PRODUCT_ID + "/reviews")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(PRODUCT_ID), eq(1L),
                        any(ReviewRequest.class))
        );
    }

    @Test
    void 리뷰_생성_실패_제품이_존재하지_않음() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        ReviewRequest reviewRequest = new ReviewRequest("내용", 5);
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new ProductNotFoundException());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/products/" + 0L + "/reviews")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(0L), eq(1L), any(ReviewRequest.class))
        );
    }

    @Test
    void 리뷰_생성_실패_회원이_존재하지_않음() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        ReviewRequest reviewRequest = new ReviewRequest("내용", 5);
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new MemberNotFoundException());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/products/" + 1L + "/reviews")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(1L), eq(1L), any(ReviewRequest.class))
        );
    }

    @Test
    void 리뷰_생성_실패_회원의_추가정보가_없음() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        ReviewRequest reviewRequest = new ReviewRequest("내용", 5);
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new RegisterNotCompletedException());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/products/" + 1L + "/reviews")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(1L), eq(1L), any(ReviewRequest.class))
        );
    }

    @Test
    void 리뷰_생성_실패_이미_리뷰가_작성되어있음() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        ReviewRequest reviewRequest = new ReviewRequest("내용", 5);
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new AlreadyWrittenReviewException());

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/products/" + 1L + "/reviews")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(
                        document("reviews-create-already-written-exception")
                );

        // then
        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(1L), eq(1L), any(ReviewRequest.class))
        );
    }

    @Test
    void 리뷰_생성_실패_리뷰가_중복되어_삽입되었음() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        ReviewRequest reviewRequest = new ReviewRequest("내용", 5);
        given(jwtProvider.validateToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn("1");
        given(reviewService.saveReviewAndInventoryProduct(anyLong(), anyLong(), any(ReviewRequest.class)))
                .willThrow(new DataIntegrityViolationException("데이터가 중복될 수 없습니다."));

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/products/" + 1L + "/reviews")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        // then
        assertAll(
                () -> verify(jwtProvider).validateToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).saveReviewAndInventoryProduct(eq(1L), eq(1L), any(ReviewRequest.class))
        );
    }

    @Test
    void 전체_리뷰_페이지_조회_성공() throws Exception {
        // given
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Product product1 = KEYBOARD_1.생성(1L);
        Product product2 = KEYBOARD_2.생성(2L);
        Member member = CORINNE.생성(1L);
        ReviewWithAuthorAndProductPageResponse reviewWithAuthorAndProductPageResponse = ReviewWithAuthorAndProductPageResponse.from(
                new SliceImpl<>(
                        List.of(REVIEW_RATING_5.작성(1L, product1, member), REVIEW_RATING_4.작성(2L, product2, member)),
                        pageable, false));

        given(reviewService.findPage(any(Pageable.class)))
                .willReturn(reviewWithAuthorAndProductPageResponse);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/reviews?page=0&size=10&sort=createdAt,desc"));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("reviews-page-get")
                );

        verify(reviewService).findPage(PageRequest.of(0, 10, Sort.by("createdAt", "id").descending()));
    }

    @Test
    void 특정_상품의_리뷰_페이지_조회() throws Exception {
        // given
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Product product = KEYBOARD_1.생성(1L);
        Member corinne = CORINNE.생성(1L);
        Member mincho = MINCHO.생성(2L);
        ReviewWithAuthorPageResponse reviewWithAuthorPageResponse = ReviewWithAuthorPageResponse.of(
                new SliceImpl<>(
                        List.of(REVIEW_RATING_5.작성(1L, product, corinne), REVIEW_RATING_4.작성(2L, product, mincho)),
                        pageable, false), null);

        given(reviewService.findPageByProductId(anyLong(), nullable(Long.class), any(Pageable.class)))
                .willReturn(reviewWithAuthorPageResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/products/1/reviews?page=0&size=10&sort=createdAt,desc"));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("reviews-by-product-page-get")
                );

        verify(reviewService).findPageByProductId(product.getId(), null,
                PageRequest.of(0, 10, Sort.by("createdAt", "id").descending()));
    }

    @Test
    void 로그인한_상태로_특정_상품의_리뷰_페이지_조회() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Product product = KEYBOARD_1.생성(1L);
        Member corinne = CORINNE.생성(1L);
        Member mincho = MINCHO.생성(2L);
        ReviewWithAuthorPageResponse reviewWithAuthorPageResponse = ReviewWithAuthorPageResponse.of(
                new SliceImpl<>(
                        List.of(REVIEW_RATING_5.작성(1L, product, corinne), REVIEW_RATING_4.작성(2L, product, mincho)),
                        pageable, false), corinne.getId());

        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(reviewService.findPageByProductId(anyLong(), eq(corinne.getId()), any(Pageable.class)))
                .willReturn(reviewWithAuthorPageResponse);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/products/1/reviews?page=0&size=10&sort=createdAt,desc")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("reviews-by-product-page-get")
                );

        assertAll(
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(reviewService).findPageByProductId(product.getId(), corinne.getId(),
                        PageRequest.of(0, 10, Sort.by("createdAt", "id").descending()))
        );
    }

    @Test
    void 특정_상품의_리뷰_페이지_조회_실패_상품_존재_하지않음() throws Exception {
        // given
        PageRequest pageable = PageRequest.of(0, 150,
                Sort.by("rating", "id").descending());

        given(reviewService.findPageByProductId(anyLong(), nullable(Long.class), any(Pageable.class)))
                .willThrow(new ProductNotFoundException());

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/products/" + 0L + "/reviews?size=150&page=0&sort=rating,desc"));

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

        verify(reviewService).findPageByProductId(0L, null, pageable);
    }

    @Test
    void 리뷰_수정_성공() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        ReviewRequest updateRequest = new ReviewRequest("수정된 내용", 4);
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(memberId, Role.USER));
        willDoNothing().given(reviewService)
                .update(eq(reviewId), eq(memberId), any(ReviewRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/reviews/" + reviewId)
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)));

        // then
        resultActions.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(
                        document("reviews-update")
                );

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).update(eq(reviewId), eq(memberId), any(ReviewRequest.class))
        );
    }

    @Test
    void 리뷰_수정_실패_회원_존재하지_않음() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        ReviewRequest updateRequest = new ReviewRequest("수정된 내용", 4);
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(memberId, Role.USER));
        willThrow(new MemberNotFoundException()).given(reviewService)
                .update(eq(reviewId), eq(memberId), any(ReviewRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/api/v1/reviews/" + reviewId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).update(eq(reviewId), eq(memberId), any(ReviewRequest.class))
        );
    }

    @Test
    void 리뷰_수정_실패_리뷰_존재하지_않음() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        ReviewRequest updateRequest = new ReviewRequest("수정된 내용", 4);
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(memberId, Role.USER));
        willThrow(new ReviewNotFoundException()).given(reviewService)
                .update(eq(reviewId), eq(memberId), any(ReviewRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/api/v1/reviews/" + reviewId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).update(eq(reviewId), eq(memberId), any(ReviewRequest.class))
        );
    }

    @Test
    void 리뷰_수정_실패_작성자가_아님() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        ReviewRequest updateRequest = new ReviewRequest("수정된 내용", 4);
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(memberId, Role.USER));
        willThrow(new NotAuthorException()).given(reviewService)
                .update(eq(reviewId), eq(memberId), any(ReviewRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                put("/api/v1/reviews/" + reviewId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
        );

        // then
        resultActions.andExpect(status().isForbidden())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).update(eq(reviewId), eq(memberId), any(ReviewRequest.class))
        );
    }

    @Test
    void 리뷰_삭제_성공() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(memberId, Role.USER));
        willDoNothing().given(reviewService)
                .delete(reviewId, memberId);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/reviews/" + reviewId)
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader));

        // then
        resultActions.andExpect(status().isNoContent())
                .andDo(print())
                .andDo(
                        document("reviews-delete")
                );

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).delete(reviewId, memberId)
        );
    }

    @Test
    void 리뷰_삭제_실패_회원_존재하지_않음() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(memberId, Role.USER));
        willThrow(new MemberNotFoundException()).given(reviewService)
                .delete(reviewId, memberId);

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/reviews/" + reviewId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).delete(reviewId, memberId)
        );
    }

    @Test
    void 리뷰_삭제_실패_리뷰_존재하지_않음() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(memberId, Role.USER));
        willThrow(new ReviewNotFoundException()).given(reviewService)
                .delete(reviewId, memberId);

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/reviews/" + reviewId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).delete(reviewId, memberId)
        );
    }

    @Test
    void 리뷰_삭제_실패_작성자가_아님() throws Exception {
        // given
        String authorizationHeader = "Bearer Token";
        Long reviewId = 1L;
        Long memberId = 1L;
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(memberId, Role.USER));
        willThrow(new NotAuthorException()).given(reviewService)
                .delete(reviewId, memberId);

        // when
        ResultActions resultActions = mockMvc.perform(
                delete("/api/v1/reviews/" + reviewId)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        );

        // then
        resultActions.andExpect(status().isForbidden())
                .andDo(print());

        assertAll(
                () -> verify(jwtProvider).isValidToken(authorizationHeader),
                () -> verify(jwtProvider).getPayload(authorizationHeader),
                () -> verify(reviewService).delete(reviewId, memberId)
        );
    }

    @Test
    void 회원_아이디로_리뷰_목록_조회_성공() throws Exception {
        // given
        Long memberId = 1L;
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        given(reviewService.findPageByMemberId(anyLong(), any(Pageable.class)))
                .willReturn(ReviewWithProductPageResponse.from(
                        new SliceImpl<>(List.of(
                                REVIEW_RATING_4.작성(2L, KEYBOARD_2.생성(2L), CORINNE.생성(memberId)),
                                REVIEW_RATING_5.작성(1L, KEYBOARD_1.생성(1L), CORINNE.생성(memberId))),
                                pageable, false)));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/" + memberId + "/reviews?size=10&page=0&sort=createdAt,desc"));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("reviews-page-get-by-memberId"));
        verify(reviewService).findPageByMemberId(memberId,
                PageRequest.of(0, 10, Sort.by("createdAt", "id").descending()));
    }

    @Test
    void 내_리뷰_목록_조회_성공() throws Exception {
        // given
        Long memberId = 1L;
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        String authorizationHeader = "Bearer Token";
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.USER));
        given(reviewService.findPageByMemberId(anyLong(), any(Pageable.class)))
                .willReturn(ReviewWithProductPageResponse.from(
                        new SliceImpl<>(List.of(
                                REVIEW_RATING_4.작성(2L, KEYBOARD_2.생성(2L), CORINNE.생성(memberId)),
                                REVIEW_RATING_5.작성(1L, KEYBOARD_1.생성(1L), CORINNE.생성(memberId))),
                                pageable, false)));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/members/me/reviews?size=10&page=0&sort=createdAt,desc")
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("reviews-page-get-own"));
        verify(reviewService).findPageByMemberId(memberId,
                PageRequest.of(0, 10, Sort.by("createdAt", "id").descending()));
    }

    @Test
    void 인벤토리_아이디로_리뷰_조회_성공() throws Exception {
        // given
        Long inventoryId = 1L;
        given(reviewService.findByInventoryProductId(inventoryId))
                .willReturn(ReviewWithProductResponse.from(REVIEW_RATING_1.작성(KEYBOARD_1.생성(1L), CORINNE.생성(1L))));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/inventoryProducts/" + inventoryId + "/reviews")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("reviews-get-by-inventoryProductId"));
        verify(reviewService).findByInventoryProductId(inventoryId);
    }
}
