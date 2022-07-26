package com.woowacourse.f12.domain.review;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Keyboard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r where r.keyboard.id = :productId")
    Slice<Review> findPageByProductId(Long productId, Pageable pageable);

    Slice<Review> findPageBy(Pageable pageable);

    boolean existsByMemberAndKeyboard(Member member, Keyboard keyboard);
}
