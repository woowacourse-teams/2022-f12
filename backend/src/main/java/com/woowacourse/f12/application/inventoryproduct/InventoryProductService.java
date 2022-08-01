package com.woowacourse.f12.application.inventoryproduct;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.request.inventoryproduct.ProfileProductRequest;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductsResponse;
import com.woowacourse.f12.exception.internalserver.SqlUpdateException;
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
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        updateProfileProduct(member, profileProductRequest);
    }

    private void updateProfileProduct(final Member member, final ProfileProductRequest profileProductRequest) {
        final List<Long> selectedInventoryProductIds = profileProductRequest.getSelectedInventoryProductIds();
        inventoryProductRepository.updateBulkProfileProductByMember(member);
        final int updateCount = inventoryProductRepository.updateBulkProfileProductByMemberAndIds(member,
                selectedInventoryProductIds);
        if (updateCount != selectedInventoryProductIds.size()) {
            throw new SqlUpdateException();
        }
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
