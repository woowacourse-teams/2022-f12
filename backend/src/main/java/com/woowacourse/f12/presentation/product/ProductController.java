package com.woowacourse.f12.presentation.product;

import com.woowacourse.f12.application.product.ProductService;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ProductPageResponse> showPage(@RequestParam(required = false) final CategoryConstant category,
                                                        final Pageable pageable) {
        return ResponseEntity.ok().body(productService.findPage(category, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> show(@PathVariable final Long id) {
        return ResponseEntity.ok().body(productService.findById(id));
    }
}
