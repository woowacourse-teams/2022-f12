package com.woowacourse.f12.application;

import com.woowacourse.f12.domain.InventoryProduct;
import com.woowacourse.f12.domain.InventoryProductRepository;
import com.woowacourse.f12.domain.MemberRepository;
import com.woowacourse.f12.dto.request.ProfileProductRequest;
import com.woowacourse.f12.exception.InventoryItemNotFoundException;
import com.woowacourse.f12.exception.MemberNotFoundException;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final InventoryProductRepository inventoryProductRepository;

    public MemberService(final MemberRepository memberRepository,
                         final InventoryProductRepository inventoryProductRepository) {
        this.memberRepository = memberRepository;
        this.inventoryProductRepository = inventoryProductRepository;
    }

    @Transactional
    public void updateProfileProducts(final Long memberId, final ProfileProductRequest profileProductRequest) {
        validateMember(memberId);
        if (!Objects.isNull(profileProductRequest.getSelectedInventoryProductId())) {
            updateProfileProduct(profileProductRequest.getSelectedInventoryProductId(), true);
        }
        if (!Objects.isNull(profileProductRequest.getUnselectedInventoryProductId())) {
            updateProfileProduct(profileProductRequest.getUnselectedInventoryProductId(), false);
        }
    }

    private void validateMember(final Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private void updateProfileProduct(final Long inventoryItemId, final boolean isSelected) {
        final InventoryProduct inventoryProduct = inventoryProductRepository.findById(inventoryItemId)
                .orElseThrow(InventoryItemNotFoundException::new);
        inventoryProduct.updateIsSelected(isSelected);
    }
}
