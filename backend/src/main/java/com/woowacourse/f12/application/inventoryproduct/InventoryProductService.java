package com.woowacourse.f12.application.inventoryproduct;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProducts;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.request.inventoryproduct.ProfileProductRequest;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductsResponse;
import com.woowacourse.f12.exception.badrequest.InvalidProfileProductUpdateException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final List<Long> selectedInventoryProductIds = profileProductRequest.getSelectedInventoryProductIds();
        validateUpdatable(member, selectedInventoryProductIds);
        cancelProfileProducts(member);
        registerProfileProducts(member, selectedInventoryProductIds);
    }

    private void validateUpdatable(final Member member, final List<Long> selectedInventoryProductIds) {
        final InventoryProducts selectedInventoryProducts = new InventoryProducts(inventoryProductRepository.findAllById(selectedInventoryProductIds));
        if (!member.getInventoryProducts().contains(selectedInventoryProducts)) {
            throw new InvalidProfileProductUpdateException();
        }
    }

    private void cancelProfileProducts(final Member member) {
        inventoryProductRepository.updateBulkProfileProductByMember(member, false);
    }

    private void registerProfileProducts(final Member member, final List<Long> selectedInventoryProductIds) {
        inventoryProductRepository.updateBulkProfileProductByMemberAndIds(member, selectedInventoryProductIds, true);
    }

    public InventoryProductsResponse findByMemberId(final Long memberId) {
        validateMember(memberId);
        final List<InventoryProduct> inventoryProducts = inventoryProductRepository.findByMemberId(memberId);
        return InventoryProductsResponse.from(inventoryProducts);
    }

    private void validateMember(final Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }
    }
}
