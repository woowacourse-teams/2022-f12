package com.woowacourse.f12.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository<Following, Long> {

    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
