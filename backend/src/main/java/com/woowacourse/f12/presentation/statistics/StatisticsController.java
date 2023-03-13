package com.woowacourse.f12.presentation.statistics;

import com.woowacourse.f12.application.statistics.StatisticsService;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(final StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/products/{productId}/statistics")
    public ProductStatisticsResponse showStatistics(@PathVariable final Long productId) {
        return statisticsService.calculateMemberStatisticsById(productId);
    }
}
