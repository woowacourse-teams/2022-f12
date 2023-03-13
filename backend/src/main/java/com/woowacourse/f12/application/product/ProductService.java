package com.woowacourse.f12.application.product;

import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.dto.request.product.ProductCreateRequest;
import com.woowacourse.f12.dto.request.product.ProductSearchRequest;
import com.woowacourse.f12.dto.request.product.ProductUpdateRequest;
import com.woowacourse.f12.dto.response.PopularProductsResponse;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.exception.notfound.ProductNotFoundException;
import com.woowacourse.f12.presentation.product.CategoryConstant;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final PopularProductsCreator popularProductCreator;
    private final ApplicationEventPublisher eventPublisher;

    public ProductService(final ProductRepository productRepository,
                          final PopularProductsCreator popularProductCreator,
                          final ApplicationEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.popularProductCreator = popularProductCreator;
        this.eventPublisher = eventPublisher;
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
        productRepository.delete(target);
        final ProductDeletedEvent event = new ProductDeletedEvent(this, productId);
        eventPublisher.publishEvent(event);
    }

    public PopularProductsResponse findPopularProducts(final int size) {
        final List<Product> products = popularProductCreator.create(size,
                productRepository::findByReviewCountGreaterThanEqualAndRatingGreaterThanEqual);
        return PopularProductsResponse.from(products);
    }
}
