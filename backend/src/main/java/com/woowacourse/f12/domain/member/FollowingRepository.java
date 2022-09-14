package com.woowacourse.f12.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowingRepository extends JpaRepository<Following, Long> {

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Optional<Following> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    List<Following> findByFollowerIdAndFollowingIdIn(Long followerId, List<Long> followingIds);
}
