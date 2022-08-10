package com.woowacourse.f12.presentation.product;

import com.woowacourse.f12.application.product.ProductService;
import com.woowacourse.f12.dto.request.product.ProductSearchRequest;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ProductPageResponse> showPage(@ModelAttribute final ProductSearchRequest productSearchRequest,
                                                        final Pageable pageable) {
        return ResponseEntity.ok(productService.findBySearchConditions(productSearchRequest, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> show(@PathVariable final Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<ProductStatisticsResponse> showStatistics(@PathVariable final Long id) {
        return ResponseEntity.ok(productService.calculateMemberStatisticsById(id));
    }
}
