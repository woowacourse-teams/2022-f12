package com.woowacourse.f12.domain.inventoryproduct;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryProductRepository extends JpaRepository<InventoryProduct, Long> {

    List<InventoryProduct> findByMemberId(Long memberId);

    boolean existsByMemberAndProduct(Member member, Product product);
}
