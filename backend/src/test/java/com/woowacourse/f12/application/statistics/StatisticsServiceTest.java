package com.woowacourse.f12.application.statistics;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.MID_LEVEL;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.ETC;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.MID_LEVEL_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.NONE_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.ETC_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.FRONTEND_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.MOBILE_CONSTANT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.domain.statistics.CareerLevelCount;
import com.woowacourse.f12.domain.statistics.JobTypeCount;
import com.woowacourse.f12.domain.statistics.StatisticsRepository;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StatisticsRepository statisticsRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void 특정_제품의_사용자의_연차와_직군의_비율을_반환한다() {
        // given
        Long productId = 1L;
        given(productRepository.existsById(productId))
                .willReturn(true);
        given(statisticsRepository.findCareerLevelCountByProductId(productId))
                .willReturn(List.of(new CareerLevelCount(JUNIOR, 1), new CareerLevelCount(MID_LEVEL, 1)));
        given(statisticsRepository.findJobTypeCountByProductId(productId))
                .willReturn(List.of(new JobTypeCount(BACKEND, 1), new JobTypeCount(ETC, 1)));

        // when
        ProductStatisticsResponse productStatisticsResponse = statisticsService.calculateMemberStatisticsById(
                productId);

        // then
        assertAll(
                () -> verify(productRepository).existsById(productId),
                () -> verify(statisticsRepository).findCareerLevelCountByProductId(productId),
                () -> verify(statisticsRepository).findJobTypeCountByProductId(productId),
                () -> assertThat(productStatisticsResponse.getCareerLevel()).usingRecursiveComparison()
                        .isEqualTo(Map.of(NONE_CONSTANT, 0.0, JUNIOR_CONSTANT, 0.5, MID_LEVEL_CONSTANT, 0.5,
                                SENIOR_CONSTANT, 0.0)),
                () -> assertThat(productStatisticsResponse.getJobType()).usingRecursiveComparison()
                        .isEqualTo(Map.of(FRONTEND_CONSTANT, 0.0, BACKEND_CONSTANT, 0.5, MOBILE_CONSTANT, 0.0,
                                ETC_CONSTANT, 0.5))
        );
    }
}
