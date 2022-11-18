package com.woowacourse.f12.presentation.inventoryproduct;

import com.woowacourse.f12.application.auth.token.MemberPayload;
import com.woowacourse.f12.application.inventoryproduct.InventoryProductService;
import com.woowacourse.f12.dto.request.inventoryproduct.ProfileProductRequest;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductsResponse;
import com.woowacourse.f12.presentation.auth.Login;
import com.woowacourse.f12.presentation.auth.VerifiedMember;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class InventoryProductController {

    private final InventoryProductService inventoryProductService;

    public InventoryProductController(final InventoryProductService inventoryProductService) {
        this.inventoryProductService = inventoryProductService;
    }

    @PatchMapping("/members/inventoryProducts")
    @Login
    public ResponseEntity<Void> updateProfileProducts(
            @RequestBody @Valid final ProfileProductRequest profileProductRequest,
            @VerifiedMember final MemberPayload memberPayload) {
        inventoryProductService.updateProfileProducts(memberPayload.getId(), profileProductRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members/inventoryProducts")
    @Login
    public InventoryProductsResponse showMyInventoryProducts(@VerifiedMember final MemberPayload memberPayload) {
        return inventoryProductService.findByMemberId(memberPayload.getId());
    }

    @GetMapping("/members/{memberId}/inventoryProducts")
    public InventoryProductsResponse show(@PathVariable final Long memberId) {
        return inventoryProductService.findByMemberId(memberId);
    }
}
