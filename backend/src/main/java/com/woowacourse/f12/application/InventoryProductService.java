package com.woowacourse.f12.application;

import com.woowacourse.f12.domain.InventoryProduct;
import com.woowacourse.f12.domain.InventoryProductRepository;
import com.woowacourse.f12.domain.MemberRepository;
import com.woowacourse.f12.dto.request.ProfileProductRequest;
import com.woowacourse.f12.dto.response.InventoryProductsResponse;
import com.woowacourse.f12.exception.InvalidProfileProductException;
import com.woowacourse.f12.exception.InventoryItemNotFoundException;
import com.woowacourse.f12.exception.MemberNotFoundException;
import java.util.List;
import java.util.Objects;
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
        validateInvalidProfileProductRequest(profileProductRequest);
        updateProfileProduct(profileProductRequest);
    }

    private void validateInvalidProfileProductRequest(final ProfileProductRequest profileProductRequest) {
        if (Objects.isNull(profileProductRequest.getSelectedInventoryProductId()) && Objects.isNull(
                profileProductRequest.getUnselectedInventoryProductId())) {
            throw new InvalidProfileProductException();
        }
    }

    private void updateProfileProduct(final ProfileProductRequest profileProductRequest) {
        if (!Objects.isNull(profileProductRequest.getSelectedInventoryProductId())) {
            updateProfileProduct(profileProductRequest.getSelectedInventoryProductId(), true);
        }
        if (!Objects.isNull(profileProductRequest.getUnselectedInventoryProductId())) {
            updateProfileProduct(profileProductRequest.getUnselectedInventoryProductId(), false);
        }
    }

    private void updateProfileProduct(final Long inventoryItemId, final boolean selected) {
        final InventoryProduct inventoryProduct = inventoryProductRepository.findById(inventoryItemId)
                .orElseThrow(InventoryItemNotFoundException::new);
        inventoryProduct.updateSelected(selected);
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
