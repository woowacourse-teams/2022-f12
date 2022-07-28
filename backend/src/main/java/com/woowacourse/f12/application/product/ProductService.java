package com.woowacourse.f12.application.product;

import com.woowacourse.f12.domain.product.Category;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.exception.notfound.KeyboardNotFoundException;
import com.woowacourse.f12.presentation.product.CategoryConstant;
import java.util.Objects;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse findById(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(KeyboardNotFoundException::new);
        return ProductResponse.from(product);
    }

    public ProductPageResponse findPage(final CategoryConstant categoryConstant, final Pageable pageable) {
        if (Objects.isNull(categoryConstant)) {
            return ProductPageResponse.from(productRepository.findPageBy(pageable));
        }

        final Category category = categoryConstant.toCategory();
        return ProductPageResponse.from(productRepository.findPageByCategory(category, pageable));
    }
}
