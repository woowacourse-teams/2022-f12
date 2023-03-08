package com.woowacourse.f12.presentation.statistics;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.MID_LEVEL;
import static com.woowacourse.f12.domain.member.CareerLevel.NONE;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.ETC;
import static com.woowacourse.f12.domain.member.JobType.FRONTEND;
import static com.woowacourse.f12.domain.member.JobType.MOBILE;
import static com.woowacourse.f12.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import com.woowacourse.f12.exception.notfound.ProductNotFoundException;
import com.woowacourse.f12.presentation.PresentationTest;
import com.woowacourse.f12.support.ErrorCodeSnippet;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

public class StatisticsControllerTest extends PresentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 특정_제품에_대한_사용자_통계_조회_성공() throws Exception {
        // given
        Map<CareerLevel, Double> careerLevel = Map.of(NONE, 0.0, JUNIOR, 0.5,
                MID_LEVEL, 0.0, SENIOR, 0.5);
        Map<JobType, Double> jobType = Map.of(FRONTEND, 0.33, BACKEND,
                0.33, MOBILE, 0.33, ETC, 0.0);
        given(statisticsService.calculateMemberStatisticsById(anyLong()))
                .willReturn(ProductStatisticsResponse.of(careerLevel, jobType));

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/products/1/statistics")
        );

        // then
        resultActions.andExpect(status().isOk())
                .andDo(document("products-member-statistics-get", new ErrorCodeSnippet(PRODUCT_NOT_FOUND)))
                .andDo(print());

        verify(statisticsService).calculateMemberStatisticsById(1L);
    }

    @Test
    void 특정_제품에_대한_사용자_통계_조회_실패_제품이_존재하지_않을_경우() throws Exception {
        // given
        given(statisticsService.calculateMemberStatisticsById(anyLong()))
                .willThrow(new ProductNotFoundException());

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/products/1/statistics"));

        // then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());

        verify(statisticsService).calculateMemberStatisticsById(1L);
    }
}
