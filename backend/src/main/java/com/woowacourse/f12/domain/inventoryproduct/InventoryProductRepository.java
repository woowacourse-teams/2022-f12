package com.woowacourse.f12.domain.inventoryproduct;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventoryProductRepository extends JpaRepository<InventoryProduct, Long> {

    List<InventoryProduct> findByMemberId(Long memberId);

    Optional<InventoryProduct> findByMemberIdAndProductId(Long memberId, Long productId);

    boolean existsByMemberAndProduct(Member member, Product product);

    @Modifying(clearAutomatically = true)
    @Query("update InventoryProduct i set i.selected = :selected where i.member = :member")
    int updateBulkProfileProductByMember(Member member, boolean selected);

    @Modifying(clearAutomatically = true)
    @Query("update InventoryProduct i set i.selected = :selected "
            + "where i.member = :member and i.id in :selectedInventoryProductIds")
    int updateBulkProfileProductByMemberAndIds(Member member, List<Long> selectedInventoryProductIds, boolean selected);
}
