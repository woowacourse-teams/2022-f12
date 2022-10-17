package com.woowacourse.f12.domain.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByGitHubId(String gitHubId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update Member m set m.followerCount = (select count(f) from Following f where f.followingId = m.id)")
    void updateFollowerCountBatch();
}
