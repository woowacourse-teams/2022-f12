package com.woowacourse.f12.presentation;

import com.woowacourse.f12.application.InventoryProductService;
import com.woowacourse.f12.dto.request.ProfileProductRequest;
import com.woowacourse.f12.dto.response.InventoryProductsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class InventoryProductController {

    private final InventoryProductService inventoryProductService;

    public InventoryProductController(final InventoryProductService inventoryProductService) {
        this.inventoryProductService = inventoryProductService;
    }

    @PatchMapping("/inventoryProducts")
    @LoginRequired
    public ResponseEntity<Void> updateProfileProducts(@RequestBody final ProfileProductRequest profileProductRequest,
                                                      @VerifiedMember final Long memberId) {
        inventoryProductService.updateProfileProducts(memberId, profileProductRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/inventoryProducts")
    @LoginRequired
    public ResponseEntity<InventoryProductsResponse> showMyInventoryProducts(@VerifiedMember final Long memberId) {
        final InventoryProductsResponse inventoryProductsResponse = inventoryProductService.findByMemberId(memberId);
        return ResponseEntity.ok(inventoryProductsResponse);
    }

    @GetMapping("/{memberId}/inventoryProducts")
    public ResponseEntity<InventoryProductsResponse> show(@PathVariable final Long memberId) {
        final InventoryProductsResponse inventoryProductsResponse = inventoryProductService.findByMemberId(memberId);
        return ResponseEntity.ok(inventoryProductsResponse);
    }
}
