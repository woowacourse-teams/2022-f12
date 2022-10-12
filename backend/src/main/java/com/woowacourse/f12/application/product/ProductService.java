package com.woowacourse.f12.application.product;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.domain.review.CareerLevelCount;
import com.woowacourse.f12.domain.review.JobTypeCount;
import com.woowacourse.f12.domain.review.MemberInfoStatistics;
import com.woowacourse.f12.domain.review.ReviewRepository;
import com.woowacourse.f12.dto.request.product.ProductCreateRequest;
import com.woowacourse.f12.dto.request.product.ProductSearchRequest;
import com.woowacourse.f12.dto.request.product.ProductUpdateRequest;
import com.woowacourse.f12.dto.response.PopularProductsResponse;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import com.woowacourse.f12.exception.notfound.ProductNotFoundException;
import com.woowacourse.f12.presentation.product.CategoryConstant;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final InventoryProductRepository inventoryProductRepository;
    private final PopularProductStrategy popularProductStrategy;

    public ProductService(final ProductRepository productRepository, final ReviewRepository reviewRepository,
                          final InventoryProductRepository inventoryProductRepository,
                          final PopularProductStrategy popularProductStrategy) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.inventoryProductRepository = inventoryProductRepository;
        this.popularProductStrategy = popularProductStrategy;
    }

    @Transactional
    public Long save(final ProductCreateRequest productCreateRequest) {
        return productRepository.save(productCreateRequest.toProduct())
                .getId();
    }

    public ProductResponse findById(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        return ProductResponse.from(product);
    }

    public ProductPageResponse findBySearchConditions(final ProductSearchRequest productSearchRequest,
                                                      final Pageable pageable) {
        final Slice<Product> slice = findProductBySearchConditions(productSearchRequest, pageable);
        return ProductPageResponse.from(slice);
    }

    private Slice<Product> findProductBySearchConditions(final ProductSearchRequest productSearchRequest,
                                                         final Pageable pageable) {
        final Category category = parseCategory(productSearchRequest);
        if (productSearchRequest.getQuery() == null && category == null) {
            return productRepository.findWithoutSearchConditions(pageable);
        }
        return productRepository.findWithSearchConditions(productSearchRequest.getQuery(), category, pageable);
    }

    private Category parseCategory(final ProductSearchRequest productSearchRequest) {
        final CategoryConstant categoryConstant = productSearchRequest.getCategory();
        if (categoryConstant == null) {
            return null;
        }
        return categoryConstant.toCategory();
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

    @Transactional
    public void update(final Long productId, final ProductUpdateRequest productUpdateRequest) {
        final Product target = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        target.update(productUpdateRequest.toProduct());
    }

    @Transactional
    public void delete(final Long productId) {
        final Product target = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        reviewRepository.deleteByProduct(target);
        inventoryProductRepository.deleteByProduct(target);
        productRepository.delete(target);
    }

    public PopularProductsResponse findPopularProducts() {
        return popularProductStrategy.getResult(productRepository::findByReviewCountAndRatingGreaterThanEqual);
    }
}
