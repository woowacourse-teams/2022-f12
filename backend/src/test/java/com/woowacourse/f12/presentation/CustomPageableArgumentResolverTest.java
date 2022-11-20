package com.woowacourse.f12.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.dto.request.product.ProductSearchRequest;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

class CustomPageableArgumentResolverTest extends PresentationTest {

    @Test
    void 페이징_실패_페이지_번호_숫자_형식_아님() throws Exception {
        // given
        given(productService.findBySearchConditions(any(ProductSearchRequest.class), any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of())));

        // when
        mockMvc.perform(get("/api/v1/products?category=keyboard&page=abc&size=10&sort=rating,desc"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        // then
        verify(productService, times(0))
                .findBySearchConditions(any(ProductSearchRequest.class), any(Pageable.class));
    }

    @Test
    void 페이징_실패_최대_페이징_크기_초과() throws Exception {
        // given
        given(productService.findBySearchConditions(any(ProductSearchRequest.class), any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of())));

        // when
        mockMvc.perform(get("/api/v1/products?category=keyboard&page=0&size=151&sort=rating,desc"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        // then
        verify(productService, times(0))
                .findBySearchConditions(any(ProductSearchRequest.class), any(Pageable.class));
    }

    @Test
    void 페이징_실패_페이징_크기_숫자_형식_아님() throws Exception {
        // given
        given(productService.findBySearchConditions(any(ProductSearchRequest.class), any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of())));

        // when
        mockMvc.perform(get("/api/v1/products?category=keyboard&page=0&size=abc&sort=rating,desc"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        // then
        verify(productService, times(0))
                .findBySearchConditions(any(ProductSearchRequest.class), any(Pageable.class));
    }

    @Test
    void 페이징_성공_페이징_값_지정_안할_경우_기본값으로_페이징() throws Exception {
        // given
        ProductSearchRequest productSearchRequest = new ProductSearchRequest(null, null);
        Pageable pageable = PageRequest.of(0, 20, Sort.by("id").descending());

        given(productService.findBySearchConditions(any(ProductSearchRequest.class), any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of())));

        // when
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(productService).findBySearchConditions(refEq(productSearchRequest), eq(pageable));
    }

    @Test
    void 평점순으로_제품_조회_API_요청_시_리뷰순을_세컨더리_정렬_기준_추가() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rating", "reviewCount", "id").descending());
        given(productService.findBySearchConditions(any(ProductSearchRequest.class), eq(pageable)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of())));

        // when
        mockMvc.perform(
                        get("/api/v1/products?page=0&size=10&sort=rating,desc"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(productService).findBySearchConditions(any(ProductSearchRequest.class), eq(pageable));
    }

    @Test
    void 리뷰순으로_제품_조회_API_요청_시_평점순을_세컨더리_정렬_기준_추가() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("reviewCount", "rating", "id").descending());
        given(productService.findBySearchConditions(any(ProductSearchRequest.class), eq(pageable)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of())));

        // when
        mockMvc.perform(
                        get("/api/v1/products?page=0&size=10&sort=reviewCount,desc"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(productService).findBySearchConditions(any(ProductSearchRequest.class), eq(pageable));
    }
}
