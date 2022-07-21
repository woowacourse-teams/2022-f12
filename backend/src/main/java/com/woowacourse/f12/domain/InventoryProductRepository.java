package com.woowacourse.f12.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryProductRepository extends JpaRepository<InventoryProduct, Long> {

    List<InventoryProduct> findByMemberId(Long memberId);

    boolean existsByMemberIdAndKeyboard(Long memberId, Keyboard keyboard);
}
