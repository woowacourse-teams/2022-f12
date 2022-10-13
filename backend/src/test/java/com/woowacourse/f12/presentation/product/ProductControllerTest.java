package com.woowacourse.f12.presentation.product;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.MID_LEVEL;
import static com.woowacourse.f12.domain.member.CareerLevel.NONE;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.ETC;
import static com.woowacourse.f12.domain.member.JobType.FRONTEND;
import static com.woowacourse.f12.domain.member.JobType.MOBILE;
import static com.woowacourse.f12.exception.ErrorCode.EXPIRED_ACCESS_TOKEN;
import static com.woowacourse.f12.exception.ErrorCode.INVALID_PAGING_PARAM;
import static com.woowacourse.f12.exception.ErrorCode.INVALID_REQUEST_BODY_TYPE;
import static com.woowacourse.f12.exception.ErrorCode.INVALID_SEARCH_PARAM;
import static com.woowacourse.f12.exception.ErrorCode.INVALID_TOKEN_FORMAT;
import static com.woowacourse.f12.exception.ErrorCode.NOT_EXIST_ACCESS_TOKEN;
import static com.woowacourse.f12.exception.ErrorCode.PERMISSION_DENIED;
import static com.woowacourse.f12.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static com.woowacourse.f12.presentation.product.CategoryConstant.KEYBOARD_CONSTANT;
import static com.woowacourse.f12.presentation.product.CategoryConstant.MONITOR_CONSTANT;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.f12.application.auth.token.JwtProvider;
import com.woowacourse.f12.application.auth.token.MemberPayload;
import com.woowacourse.f12.application.product.ProductService;
import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.member.Role;
import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.dto.request.product.ProductCreateRequest;
import com.woowacourse.f12.dto.request.product.ProductSearchRequest;
import com.woowacourse.f12.dto.request.product.ProductUpdateRequest;
import com.woowacourse.f12.dto.response.PopularProductsResponse;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import com.woowacourse.f12.exception.notfound.ProductNotFoundException;
import com.woowacourse.f12.presentation.PresentationTest;
import com.woowacourse.f12.support.ErrorCodeSnippet;
import java.util.List;
import java.util.Map;
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

@WebMvcTest(ProductController.class)
class ProductControllerTest extends PresentationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private JwtProvider jwtProvider;

    @Test
    void 키보드_목록_페이지_조회_성공() throws Exception {
        // given
        ProductSearchRequest productSearchRequest = new ProductSearchRequest(null, KEYBOARD_CONSTANT);
        Pageable pageable = PageRequest.of(0, 150, Sort.by("rating", "reviewCount", "id").descending());
        given(productService.findBySearchConditions(any(ProductSearchRequest.class), any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of(KEYBOARD_1.생성(1L)))));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/products?category=keyboard&page=0&size=150&sort=rating,desc"));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());

        verify(productService).findBySearchConditions(refEq(productSearchRequest), eq(pageable));
    }

    @Test
    void 제품_목록_페이지_조회_성공() throws Exception {
        // given
        ProductSearchRequest productSearchRequest = new ProductSearchRequest(null, null);
        Pageable pageable = PageRequest.of(0, 150, Sort.by("rating", "reviewCount", "id").descending());
        given(productService.findBySearchConditions(any(ProductSearchRequest.class), any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of(KEYBOARD_1.생성(1L)))));

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/products?page=0&size=150&sort=rating,desc"));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());

        verify(productService).findBySearchConditions(refEq(productSearchRequest), eq(pageable));
    }

    @Test
    void 잘못된_카테고리로_조회하려는_경우_예외_발생() throws Exception {
        // given
        ProductSearchRequest productSearchRequest = new ProductSearchRequest(null, KEYBOARD_CONSTANT);
        Pageable pageable = PageRequest.of(0, 150, Sort.by("rating", "id").descending());
        given(productService.findBySearchConditions(any(ProductSearchRequest.class), any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of(KEYBOARD_1.생성(1L)))));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/products?category=INVALID&page=0&size=150&sort=rating,desc"));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());

        verify(productService, times(0)).findBySearchConditions(refEq(productSearchRequest), eq(pageable));
    }

    @Test
    void 제품명과_카테고리로_제품_목록을_조회_성공() throws Exception {
        // given
        ProductSearchRequest productSearchRequest = new ProductSearchRequest("1", KEYBOARD_CONSTANT);
        Pageable pageable = PageRequest.of(0, 1);
        given(productService.findBySearchConditions(any(ProductSearchRequest.class), any(PageRequest.class)))
                .willReturn(ProductPageResponse.from(
                        new SliceImpl<>(List.of(KEYBOARD_1.생성(1L)), pageable, false))
                );

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/products?query=1&category=keyboard&page=0&size=1"));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("products-page-get",
                        new ErrorCodeSnippet(INVALID_SEARCH_PARAM, INVALID_PAGING_PARAM)))
                .andDo(print());

        verify(productService).findBySearchConditions(refEq(productSearchRequest), any(PageRequest.class));
    }

    @Test
    void 제품_단일_조회_성공() throws Exception {
        // given
        given(productService.findById(anyLong()))
                .willReturn(ProductResponse.from(KEYBOARD_1.생성(1L)));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/products/1")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("products-get", new ErrorCodeSnippet(PRODUCT_NOT_FOUND))
                );

        verify(productService).findById(1L);
    }

    @Test
    void 제품_단일_조회_실패_존재_하지_않는_아이디() throws Exception {
        // given
        given(productService.findById(anyLong()))
                .willThrow(new ProductNotFoundException());

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/products/0"));

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

        verify(productService).findById(0L);
    }

    @Test
    void 특정_제품에_대한_사용자_통계_조회_성공() throws Exception {
        // given
        Map<CareerLevel, Double> careerLevel = Map.of(NONE, 0.0, JUNIOR, 0.5,
                MID_LEVEL, 0.0, SENIOR, 0.5);
        Map<JobType, Double> jobType = Map.of(FRONTEND, 0.33, BACKEND,
                0.33, MOBILE, 0.33, ETC, 0.0);
        given(productService.calculateMemberStatisticsById(anyLong()))
                .willReturn(ProductStatisticsResponse.of(careerLevel, jobType));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/products/1/statistics")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("products-member-statistics-get", new ErrorCodeSnippet(PRODUCT_NOT_FOUND)))
                .andDo(print());

        verify(productService).calculateMemberStatisticsById(1L);
    }

    @Test
    void 특정_제품에_대한_사용자_통계_조회_실패_제품이_존재하지_않을_경우() throws Exception {
        // given
        given(productService.calculateMemberStatisticsById(anyLong()))
                .willThrow(new ProductNotFoundException());

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/products/1/statistics"));

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

        verify(productService).calculateMemberStatisticsById(1L);
    }

    @Test
    void 인기_제품을_조회힌다() throws Exception {
        // given
        Product keyboard = Product.builder()
                .id(1L)
                .name("키보드")
                .imageUrl("이미지 주소")
                .category(Category.KEYBOARD)
                .rating(4.5)
                .reviewCount(10)
                .build();
        Product mouse = Product.builder()
                .id(2L)
                .name("마우스")
                .imageUrl("이미지 주소")
                .category(Category.MOUSE)
                .rating(5)
                .reviewCount(3)
                .build();
        Product software = Product.builder()
                .id(3L)
                .name("소프트웨어")
                .imageUrl("이미지 주소")
                .category(Category.SOFTWARE)
                .rating(5)
                .reviewCount(2)
                .build();
        Product monitor = Product.builder()
                .id(4L)
                .name("모니터")
                .imageUrl("이미지 주소")
                .category(Category.MONITOR)
                .rating(4.5)
                .reviewCount(2)
                .build();
        int popularProductSize = 4;
        given(productService.findPopularProducts(popularProductSize))
                .willReturn(PopularProductsResponse.from(List.of(keyboard, mouse, software, monitor)));

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/products/popular-list?size=" + popularProductSize));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("products-popular-list-get"))
                .andDo(print());
        verify(productService).findPopularProducts(popularProductSize);
    }

    @Test
    void 어드민_계정으로_로그인_하여_제품_추가_성공() throws Exception {
        // given
        String authorizationHeader = "Bearer token";
        given(productService.save(any(ProductCreateRequest.class)))
                .willReturn(1L);
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.ADMIN));
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("keyboard", "keyborad.url",
                KEYBOARD_CONSTANT);

        // when
        ResultActions resultActions = mockMvc.perform(
                        post("/api/v1/products")
                                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(productCreateRequest))
                )
                .andDo(document("admin-products-create",
                        new ErrorCodeSnippet(INVALID_REQUEST_BODY_TYPE, NOT_EXIST_ACCESS_TOKEN, EXPIRED_ACCESS_TOKEN,
                                INVALID_TOKEN_FORMAT, PERMISSION_DENIED)))
                .andDo(print());

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/v1/products/1"));
    }

    @Test
    void 어드민_계정으로_로그인_하여_제품_수정_성공() throws Exception {
        // given
        String authorizationHeader = "Bearer token";
        willDoNothing().given(productService)
                .update(anyLong(), any(ProductUpdateRequest.class));
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.ADMIN));
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest("updatedName", "updatedURL",
                MONITOR_CONSTANT);

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/products/" + 1)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(productUpdateRequest))
                )
                .andDo(document("admin-products-update",
                        new ErrorCodeSnippet(INVALID_REQUEST_BODY_TYPE, NOT_EXIST_ACCESS_TOKEN, EXPIRED_ACCESS_TOKEN,
                                INVALID_TOKEN_FORMAT, PERMISSION_DENIED)))
                .andDo(print());

        // then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    void 어드민_계정으로_로그인_하여_제품_삭제_성공() throws Exception {
        // given
        String authorizationHeader = "Bearer token";
        willDoNothing().given(productService)
                .delete(anyLong());
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.ADMIN));
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/products/" + 1)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                )
                .andDo(document("admin-products-delete",
                        new ErrorCodeSnippet(NOT_EXIST_ACCESS_TOKEN, EXPIRED_ACCESS_TOKEN, INVALID_TOKEN_FORMAT,
                                PERMISSION_DENIED, PRODUCT_NOT_FOUND)))
                .andDo(print());

        // then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    void 어드민_계정으로_로그인_하여_존재하지_않는_제품_삭제_실패() throws Exception {
        // given
        String authorizationHeader = "Bearer token";
        willThrow(new ProductNotFoundException()).given(productService)
                .delete(anyLong());
        given(jwtProvider.getPayload(authorizationHeader))
                .willReturn(new MemberPayload(1L, Role.ADMIN));
        given(jwtProvider.isValidToken(authorizationHeader))
                .willReturn(true);

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/products/" + 1)
                        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                )
                .andDo(print());

        // then
        resultActions.andExpect(status().isNotFound());
    }
}
