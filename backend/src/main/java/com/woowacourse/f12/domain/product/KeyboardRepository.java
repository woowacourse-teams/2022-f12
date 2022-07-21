package com.woowacourse.f12.domain.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyboardRepository extends JpaRepository<Keyboard, Long> {

    Slice<Keyboard> findPageBy(Pageable pageable);
}
