package com.woowacourse.f12.application.product;

import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.domain.review.CareerLevelCount;
import com.woowacourse.f12.domain.review.JobTypeCount;
import com.woowacourse.f12.domain.review.ReviewRepository;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import com.woowacourse.f12.exception.notfound.ProductNotFoundException;
import com.woowacourse.f12.presentation.product.CategoryConstant;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public ProductService(final ProductRepository productRepository, final ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    public ProductResponse findById(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        return ProductResponse.from(product);
    }

    public ProductPageResponse findPage(final CategoryConstant categoryConstant, final Pageable pageable) {
        if (Objects.isNull(categoryConstant)) {
            return ProductPageResponse.from(productRepository.findPageBy(pageable));
        }

        final Category category = categoryConstant.toCategory();
        return ProductPageResponse.from(productRepository.findPageByCategory(category, pageable));
    }

    public ProductStatisticsResponse calculateMemberStatisticsById(final Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }
        final List<CareerLevelCount> careerLevelCounts = reviewRepository.findCareerLevelCountByProductId(productId);
        final List<JobTypeCount> jobTypeCounts = reviewRepository.findJobTypeCountByProductId(productId);

        final Map<CareerLevel, Double> careerLevel = calculateCareerLevelStatistics(careerLevelCounts);
        final Map<JobType, Double> jobType = calculateJobTypeStatistics(jobTypeCounts);

        return ProductStatisticsResponse.of(careerLevel, jobType);
    }

    private Map<CareerLevel, Double> calculateCareerLevelStatistics(
            final List<CareerLevelCount> careerLevelCounts) {
        final Map<CareerLevel, Double> careerLevel = new EnumMap<>(CareerLevel.class);
        final long totalCareerLevelCount = careerLevelCounts.stream()
                .mapToLong(CareerLevelCount::getCount)
                .sum();

        for (CareerLevel careerLevel1 : CareerLevel.values()) {
            careerLevel.put(careerLevel1, 0.0);
        }

        for (CareerLevelCount careerLevelCount : careerLevelCounts) {
            final CareerLevel key = careerLevelCount.getCareerLevel();
            careerLevel.put(key, careerLevelCount.getCount() / (double) totalCareerLevelCount);
        }

        return careerLevel;
    }

    private Map<JobType, Double> calculateJobTypeStatistics(final List<JobTypeCount> jobTypeCounts) {
        final Map<JobType, Double> jobType = new EnumMap<>(JobType.class);
        final long totalJobTypeCount = jobTypeCounts.stream()
                .mapToLong(JobTypeCount::getCount)
                .sum();

        for (JobType jobType1 : JobType.values()) {
            jobType.put(jobType1, 0.0);
        }

        for (JobTypeCount jobTypeCount : jobTypeCounts) {
            final JobType key = jobTypeCount.getJobType();
            jobType.put(key, jobTypeCount.getCount() / (double) totalJobTypeCount);
        }
        return jobType;
    }
}
