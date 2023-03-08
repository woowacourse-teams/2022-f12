package com.woowacourse.f12.application.inventoryproduct;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProducts;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.request.inventoryproduct.ProfileProductRequest;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductsResponse;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InventoryProductService {
    private final InventoryProductRepository inventoryProductRepository;

    private final MemberRepository memberRepository;

    public InventoryProductService(final InventoryProductRepository inventoryProductRepository,
                                   final MemberRepository memberRepository) {
        this.inventoryProductRepository = inventoryProductRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void updateProfileProducts(final Long memberId, final ProfileProductRequest profileProductRequest) {
        validateMember(memberId);
        final List<Long> selectedInventoryProductIds = profileProductRequest.getSelectedInventoryProductIds();
        validateUpdateSelected(memberId, selectedInventoryProductIds);
        cancelProfileProducts(memberId);
        registerProfileProducts(memberId, selectedInventoryProductIds);
    }

    private void validateMember(final Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }
    }

    private void validateUpdateSelected(final Long memberId, final List<Long> selectedInventoryProductIds) {
        final List<InventoryProduct> memberInventoryProducts = inventoryProductRepository.findWithProductByMemberId(
                memberId);
        final InventoryProducts inventoryProducts = new InventoryProducts(memberInventoryProducts);
        final List<InventoryProduct> selectedInventoryProductList = inventoryProductRepository.findAllById(
                selectedInventoryProductIds);
        final InventoryProducts selectedInventoryProducts = new InventoryProducts(selectedInventoryProductList);
        inventoryProducts.validateUpdateSelected(selectedInventoryProducts);
    }

    private void cancelProfileProducts(final Long memberId) {
        inventoryProductRepository.updateBulkProfileProductByMemberId(memberId, false);
    }

    private void registerProfileProducts(final Long memberId, final List<Long> selectedInventoryProductIds) {
        inventoryProductRepository.updateBulkProfileProductByMemberIdAndIds(memberId, selectedInventoryProductIds,
                true);
    }

    public InventoryProductsResponse findByMemberId(final Long memberId) {
        validateMember(memberId);
        final List<InventoryProduct> inventoryProducts = inventoryProductRepository.findWithProductByMemberId(memberId);
        return InventoryProductsResponse.from(inventoryProducts);
    }
}
