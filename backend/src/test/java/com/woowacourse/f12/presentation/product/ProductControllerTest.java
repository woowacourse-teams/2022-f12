package com.woowacourse.f12.presentation.product;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.MID_LEVEL;
import static com.woowacourse.f12.domain.member.CareerLevel.NONE;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.ETC;
import static com.woowacourse.f12.domain.member.JobType.FRONTEND;
import static com.woowacourse.f12.domain.member.JobType.MOBILE;
import static com.woowacourse.f12.presentation.product.CategoryConstant.KEYBOARD_CONSTANT;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_1;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.auth.JwtProvider;
import com.woowacourse.f12.application.product.ProductService;
import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import com.woowacourse.f12.exception.notfound.ProductNotFoundException;
import com.woowacourse.f12.support.AuthTokenExtractor;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@Import({AuthTokenExtractor.class, JwtProvider.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void 키보드_목록_페이지_조회_성공() throws Exception {
        // given
        given(productService.findPage(eq(KEYBOARD_CONSTANT), any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of(KEYBOARD_1.생성(1L)))));

        // when
        mockMvc.perform(get("/api/v1/products?category=keyboard&page=0&size=150&sort=rating,desc"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(productService).findPage(KEYBOARD_CONSTANT, PageRequest.of(0, 150, Sort.by("rating").descending()));
    }

    @Test
    void 제품_목록_페이지_조회_성공() throws Exception {
        // given
        given(productService.findPage(eq(null), any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of(KEYBOARD_1.생성(1L)))));

        // when
        mockMvc.perform(get("/api/v1/products?page=0&size=150&sort=rating,desc"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(productService).findPage(null, PageRequest.of(0, 150, Sort.by("rating").descending()));
    }

    @Test
    void 잘못된_카테고리로_조회하려는_경우_예외_발생() throws Exception {
        // given
        given(productService.findPage(eq(KEYBOARD_CONSTANT), any(Pageable.class)))
                .willReturn(ProductPageResponse.from(new SliceImpl<>(List.of(KEYBOARD_1.생성(1L)))));

        // when
        mockMvc.perform(get("/api/v1/products?category=INVALID&page=0&size=150&sort=rating,desc"))
                .andExpect(status().isBadRequest())
                .andDo(print());

        // then
        verify(productService, times(0)).findPage(KEYBOARD_CONSTANT,
                PageRequest.of(0, 150, Sort.by("rating").descending()));
    }

    @Test
    void 제품_단일_조회_성공() throws Exception {
        // given
        given(productService.findById(anyLong()))
                .willReturn(ProductResponse.from(KEYBOARD_1.생성(1L)));

        // when
        mockMvc.perform(get("/api/v1/products/" + 1L))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(productService).findById(1L);
    }

    @Test
    void 제품_단일_조회_실패_존재_하지_않는_아이디() throws Exception {
        // given
        given(productService.findById(anyLong()))
                .willThrow(new ProductNotFoundException());

        // when
        mockMvc.perform(get("/api/v1/products/0"))
                .andExpect(status().isNotFound())
                .andDo(print());

        // then
        verify(productService).findById(0L);
    }

    @Test
    void 특정_제품에_대한_사용자_통계_조회_성공() throws Exception {
        // given
        Map<CareerLevel, Double> careerLevel = Map.of(NONE, 0.0, JUNIOR, 0.5,
                MID_LEVEL, 0.0, SENIOR, 0.5);
        Map<JobType, Double> jobType = Map.of(FRONTEND, 0.5, BACKEND,
                0.5, MOBILE, 0.0, ETC, 0.0);
        given(productService.calculateMemberStatisticsById(anyLong()))
                .willReturn(ProductStatisticsResponse.of(careerLevel, jobType));

        // when
        mockMvc.perform(get("/api/v1/products/1/statistics"))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(productService).calculateMemberStatisticsById(1L);
    }

    @Test
    void 특정_제품에_대한_사용자_통계_조회_실패_제품이_존재하지_않을_경우() throws Exception {
        // given
        given(productService.calculateMemberStatisticsById(anyLong()))
                .willThrow(new ProductNotFoundException());

        // when
        mockMvc.perform(get("/api/v1/products/1/statistics"))
                .andExpect(status().isNotFound())
                .andDo(print());

        // then
        verify(productService).calculateMemberStatisticsById(1L);
    }
}
