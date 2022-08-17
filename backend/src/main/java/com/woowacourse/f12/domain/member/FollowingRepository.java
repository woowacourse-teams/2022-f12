package com.woowacourse.f12.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowingRepository extends JpaRepository<Following, Long> {

    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    Optional<Following> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
