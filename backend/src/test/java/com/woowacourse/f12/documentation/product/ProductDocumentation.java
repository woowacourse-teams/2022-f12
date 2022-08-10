package com.woowacourse.f12.documentation.product;

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
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_2;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.application.product.ProductService;
import com.woowacourse.f12.documentation.Documentation;
import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.dto.request.product.ProductSearchRequest;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import com.woowacourse.f12.presentation.product.ProductController;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ProductController.class)
class ProductDocumentation extends Documentation {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void 제품_단일_조회_API_문서화() throws Exception {
        // given
        Product product = KEYBOARD_1.생성(1L);
        given(productService.findById(1L))
                .willReturn(ProductResponse.from(product));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/products/1")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("products-get")
                );
    }

    @Test
    void 제품명과_카테고리로_제품_목록을_조회_API_문서화() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 1);
        given(productService.findBySearchConditions(any(ProductSearchRequest.class), any(PageRequest.class)))
                .willReturn(ProductPageResponse.from(
                        new SliceImpl<>(List.of(KEYBOARD_1.생성(1L)), pageable, false))
                );

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/products?query=1&category=keyboard&page=0&size=1"));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("products-page-get"))
                .andDo(print());
    }

    @Test
    void 특정_제품에_대한_사용자_통계_조회_API_문서화() throws Exception {
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
                .andDo(document("products-member-statistics-get"))
                .andDo(print());
    }
}
