package com.woowacourse.f12.domain.inventoryproduct;

import com.woowacourse.f12.domain.product.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InventoryProductRepository extends JpaRepository<InventoryProduct, Long> {

    @Query("select i from InventoryProduct i join fetch i.product where i.memberId = :memberId")
    List<InventoryProduct> findWithProductByMemberId(Long memberId);

    @Query("select i from InventoryProduct i join fetch i.product where i.memberId = :memberId and i.product = :product")
    Optional<InventoryProduct> findWithProductByMemberIdAndProduct(Long memberId, Product product);

    boolean existsByMemberIdAndProduct(Long memberId, Product product);

    @Modifying(clearAutomatically = true)
    @Query("update InventoryProduct i set i.selected = :selected where i.memberId = :memberId")
    int updateBulkProfileProductByMemberId(Long memberId, boolean selected);

    @Modifying(clearAutomatically = true)
    @Query("update InventoryProduct i set i.selected = :selected "
            + "where i.memberId = :memberId and i.id in :selectedInventoryProductIds")
    int updateBulkProfileProductByMemberIdAndIds(Long memberId, List<Long> selectedInventoryProductIds,
                                                 boolean selected);

    @Query("select i from InventoryProduct i join fetch i.product where i.memberId IN :memberIds")
    List<InventoryProduct> findWithProductByMemberIds(List<Long> memberIds);

    @Modifying(clearAutomatically = true)
    @Query("delete from InventoryProduct i where i.product.id=:productId")
    void deleteByProductId(Long productId);
}
