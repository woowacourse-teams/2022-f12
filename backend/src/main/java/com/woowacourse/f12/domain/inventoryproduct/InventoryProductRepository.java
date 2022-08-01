package com.woowacourse.f12.domain.inventoryproduct;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InventoryProductRepository extends JpaRepository<InventoryProduct, Long> {

    List<InventoryProduct> findByMemberId(Long memberId);

    boolean existsByMemberAndProduct(Member member, Product product);

    @Modifying(clearAutomatically = true)
    @Query("update InventoryProduct i set i.selected = false where i.member = :member")
    int updateBulkProfileProductByMember(Member member);

    @Modifying(clearAutomatically = true)
    @Query("update InventoryProduct i set i.selected = true "
            + "where i.member = :member and i.id in :selectedInventoryProductIds")
    int updateBulkProfileProductByMemberAndIds(Member member, List<Long> selectedInventoryProductIds);
}
