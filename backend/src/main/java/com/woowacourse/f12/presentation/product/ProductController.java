package com.woowacourse.f12.presentation.product;

import com.woowacourse.f12.application.product.ProductService;
import com.woowacourse.f12.dto.request.product.ProductCreateRequest;
import com.woowacourse.f12.dto.request.product.ProductSearchRequest;
import com.woowacourse.f12.dto.request.product.ProductUpdateRequest;
import com.woowacourse.f12.dto.response.product.ProductPageResponse;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.dto.response.product.ProductStatisticsResponse;
import com.woowacourse.f12.presentation.auth.Login;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    @Login(admin = true)
    public ResponseEntity<Void> create(@RequestBody final ProductCreateRequest productCreateRequest) {
        final Long savedId = productService.save(productCreateRequest);
        return ResponseEntity.created(URI.create("/api/v1/products/" + savedId))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> show(@PathVariable final Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PatchMapping("/{id}")
    @Login(admin = true)
    public ResponseEntity<Void> update(@PathVariable final Long id,
                                       @RequestBody final ProductUpdateRequest productUpdateRequest) {
        productService.update(id, productUpdateRequest);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    @Login(admin = true)
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        productService.delete(id);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<ProductStatisticsResponse> showStatistics(@PathVariable final Long id) {
        return ResponseEntity.ok(productService.calculateMemberStatisticsById(id));
    }
}
