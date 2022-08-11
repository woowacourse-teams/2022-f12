package com.woowacourse.f12.application.inventoryproduct;

import static com.woowacourse.f12.domain.product.Category.SOFTWARE;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.domain.review.Review;
import com.woowacourse.f12.domain.review.ReviewRepository;
import com.woowacourse.f12.dto.request.inventoryproduct.ProfileProductRequest;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductsResponse;
import com.woowacourse.f12.dto.response.review.ReviewResponse;
import com.woowacourse.f12.exception.badrequest.DuplicatedProfileProductCategoryException;
import com.woowacourse.f12.exception.badrequest.InvalidProfileProductCategoryException;
import com.woowacourse.f12.exception.badrequest.NotUpdatableException;
import com.woowacourse.f12.exception.notfound.InventoryProductNotFoundException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InventoryProductService {

    private final InventoryProductRepository inventoryProductRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    public InventoryProductService(final InventoryProductRepository inventoryProductRepository,
                                   final MemberRepository memberRepository, final ReviewRepository reviewRepository) {
        this.inventoryProductRepository = inventoryProductRepository;
        this.memberRepository = memberRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public void updateProfileProducts(final Long memberId, final ProfileProductRequest profileProductRequest) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        final List<Long> selectedInventoryProductIds = profileProductRequest.getSelectedInventoryProductIds();
        validateProfileProducts(selectedInventoryProductIds);
        cancelProfileProducts(member);
        registerProfileProducts(member, selectedInventoryProductIds);
    }

    private void validateProfileProducts(final List<Long> selectedInventoryProductIds) {
        final List<InventoryProduct> inventoryProducts = inventoryProductRepository.findAllById(
                selectedInventoryProductIds);
        validateNotContainsSoftware(inventoryProducts);
        validateCategoryNotDuplicated(inventoryProducts);
    }

    private void validateNotContainsSoftware(final List<InventoryProduct> inventoryProducts) {
        final boolean hasSoftware = inventoryProducts.stream()
                .map(it -> it.getProduct().getCategory())
                .anyMatch(it -> it.equals(SOFTWARE));
        if (hasSoftware) {
            throw new InvalidProfileProductCategoryException();
        }
    }

    private void validateCategoryNotDuplicated(final List<InventoryProduct> inventoryProducts) {
        final long distinctCount = inventoryProducts.stream()
                .map(it -> it.getProduct().getCategory())
                .distinct()
                .count();
        if (distinctCount != inventoryProducts.size()) {
            throw new DuplicatedProfileProductCategoryException();
        }
    }

    private void cancelProfileProducts(final Member member) {
        inventoryProductRepository.updateBulkProfileProductByMember(member, false);
    }

    private void registerProfileProducts(final Member member, final List<Long> selectedInventoryProductIds) {
        final int updatedCount = inventoryProductRepository.updateBulkProfileProductByMemberAndIds(member,
                selectedInventoryProductIds, true);
        if (updatedCount != selectedInventoryProductIds.size()) {
            throw new NotUpdatableException();
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

    public ReviewResponse findReviewById(final Long id) {
        final InventoryProduct inventoryProduct = inventoryProductRepository.findById(id)
                .orElseThrow(InventoryProductNotFoundException::new);

        final Optional<Review> review = reviewRepository.findByMemberAndProduct(
                inventoryProduct.getMember(), inventoryProduct.getProduct());

        if (review.isEmpty()) {
            return null;
        }
        return ReviewResponse.from(review.get());
    }
}
