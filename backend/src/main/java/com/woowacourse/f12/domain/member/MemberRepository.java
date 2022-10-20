package com.woowacourse.f12.domain.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByGitHubId(String gitHubId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update Member m set m.followerCount = m.followerCount + 1 where m.id = :followingMemberId")
    void increaseFollowerCount(Long followingMemberId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update Member m set m.followerCount = case m.followerCount when 0 then 0 "
            + "else (m.followerCount - 1) end where m.id = :followingMemberId")
    void decreaseFollowerCount(Long followingMemberId);
}
