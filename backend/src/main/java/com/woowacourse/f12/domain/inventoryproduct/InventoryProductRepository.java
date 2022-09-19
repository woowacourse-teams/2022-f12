package com.woowacourse.f12.domain.inventoryproduct;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventoryProductRepository extends JpaRepository<InventoryProduct, Long> {

    @Query("select i from InventoryProduct i join fetch i.product where i.member.id = :memberId")
    List<InventoryProduct> findWithProductByMemberId(Long memberId);

    @Query("select i from InventoryProduct i join fetch i.product where i.member = :member and i.product = :product")
    Optional<InventoryProduct> findWithProductByMemberAndProduct(Member member, Product product);

    boolean existsByMemberAndProduct(Member member, Product product);

    @Modifying(clearAutomatically = true)
    @Query("update InventoryProduct i set i.selected = :selected where i.member = :member")
    int updateBulkProfileProductByMember(Member member, boolean selected);

    @Modifying(clearAutomatically = true)
    @Query("update InventoryProduct i set i.selected = :selected "
            + "where i.member = :member and i.id in :selectedInventoryProductIds")
    int updateBulkProfileProductByMemberAndIds(Member member, List<Long> selectedInventoryProductIds, boolean selected);

    @Query("select i from InventoryProduct i join fetch i.product where i.member IN :members")
    List<InventoryProduct> findWithProductByMembers(List<Member> members);
}
