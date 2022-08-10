package com.woowacourse.f12.application.product;

import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.domain.review.CareerLevelCount;
import com.woowacourse.f12.domain.review.JobTypeCount;
import com.woowacourse.f12.domain.review.MemberInfoStatistics;
import com.woowacourse.f12.domain.review.ReviewRepository;
import com.woowacourse.f12.dto.request.product.ProductSearchRequest;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import com.woowacourse.f12.exception.notfound.ProductNotFoundException;
import com.woowacourse.f12.presentation.product.CategoryConstant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
        final Map<CareerLevel, Double> careerLevel = calculateWithCareerLevel(productId);
        final Map<JobType, Double> jobType = calculateWithJobType(productId);
        return ProductStatisticsResponse.of(careerLevel, jobType);
    }

    private Map<CareerLevel, Double> calculateWithCareerLevel(final Long productId) {
        final List<CareerLevelCount> careerLevelCounts = reviewRepository.findCareerLevelCountByProductId(productId);
        final MemberInfoStatistics<CareerLevelCount, CareerLevel> careerLevelStatistics = new MemberInfoStatistics<>(
                careerLevelCounts);
        return careerLevelStatistics.calculateStatistics(CareerLevel.values());
    }

    private Map<JobType, Double> calculateWithJobType(final Long productId) {
        final List<JobTypeCount> jobTypeCounts = reviewRepository.findJobTypeCountByProductId(productId);
        final MemberInfoStatistics<JobTypeCount, JobType> jobTypeStatistics = new MemberInfoStatistics<>(jobTypeCounts);
        return jobTypeStatistics.calculateStatistics(JobType.values());
    }

    public ProductPageResponse findBySearchConditions(final ProductSearchRequest productSearchRequest, final Pageable pageable) {
        final Category category = parseCategory(productSearchRequest);
        final Slice<Product> slice = productRepository.findBySearchConditions(productSearchRequest.getQuery(), category, pageable);
        return ProductPageResponse.from(slice);
    }

    private Category parseCategory(final ProductSearchRequest productSearchRequest) {
        final CategoryConstant categoryConstant = productSearchRequest.getCategory();
        if (Objects.isNull(categoryConstant)) {
            return null;
        }
        return categoryConstant.toCategory();
    }
}
