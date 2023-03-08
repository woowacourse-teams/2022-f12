package com.woowacourse.f12.application.statistics;

import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.domain.statistics.CareerLevelCount;
import com.woowacourse.f12.domain.statistics.JobTypeCount;
import com.woowacourse.f12.domain.statistics.MemberInfoStatistics;
import com.woowacourse.f12.domain.statistics.StatisticsRepository;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import com.woowacourse.f12.exception.notfound.ProductNotFoundException;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StatisticsService {

    private final ProductRepository productRepository;
    private final StatisticsRepository statisticsRepository;

    public StatisticsService(final ProductRepository productRepository,
                             final StatisticsRepository statisticsRepository) {
        this.productRepository = productRepository;
        this.statisticsRepository = statisticsRepository;
    }

    public ProductStatisticsResponse calculateMemberStatisticsById(final Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }
        final Map<CareerLevel, Double> careerLevel = calculateWithCareerLevel(productId);
        final Map<JobType, Double> jobType = calculateWithJobType(productId);
        return ProductStatisticsResponse.of(careerLevel, jobType);
    }

    private Map<CareerLevel, Double> calculateWithCareerLevel(final Long productId) {
        final List<CareerLevelCount> careerLevelCounts = statisticsRepository.findCareerLevelCountByProductId(
                productId);
        final MemberInfoStatistics<CareerLevelCount, CareerLevel> careerLevelStatistics = new MemberInfoStatistics<>(
                careerLevelCounts);
        return careerLevelStatistics.calculateStatistics(CareerLevel.values());
    }

    private Map<JobType, Double> calculateWithJobType(final Long productId) {
        final List<JobTypeCount> jobTypeCounts = statisticsRepository.findJobTypeCountByProductId(productId);
        final MemberInfoStatistics<JobTypeCount, JobType> jobTypeStatistics = new MemberInfoStatistics<>(jobTypeCounts);
        return jobTypeStatistics.calculateStatistics(JobType.values());
    }
}
