package com.woowacourse.f12.presentation;

import com.woowacourse.f12.application.InventoryProductService;
import com.woowacourse.f12.dto.response.InventoryProductsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members/inventoryProducts")
public class InventoryProductController {

    private final InventoryProductService inventoryProductService;

    public InventoryProductController(final InventoryProductService inventoryProductService) {
        this.inventoryProductService = inventoryProductService;
    }

    @GetMapping
    @LoginRequired
    public ResponseEntity<InventoryProductsResponse> show(@VerifiedMember final Long memberId) {
        final InventoryProductsResponse inventoryProductsResponse = inventoryProductService.findByMemberId(memberId);
        return ResponseEntity.ok(inventoryProductsResponse);
    }
}
