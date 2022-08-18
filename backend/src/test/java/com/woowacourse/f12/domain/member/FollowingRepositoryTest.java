package com.woowacourse.f12.domain.member;

import com.woowacourse.f12.config.JpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
class FollowingRepositoryTest {

    @Autowired
    private FollowingRepository followingRepository;

    @Test
    void 팔로워_아이디와_팔로이_아이디로_팔로잉이_존재하는지_확인한다() {
        // given
        Long followerId = 1L;
        Long followeeId = 2L;
        Following following = Following.builder()
                .followerId(followerId)
                .followeeId(followeeId)
                .build();
        followingRepository.save(following);

        // when
        boolean result = followingRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 팔로워_아이디와_팔로이_아이디로_팔로잉을_조회한다() {
        // given
        Long followerId = 1L;
        Long followeeId = 2L;
        Following following = Following.builder()
                .followerId(followerId)
                .followeeId(followeeId)
                .build();
        Following expected = followingRepository.save(following);

        // when
        Following actual = followingRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).get();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 팔로워_아이디와_팔로이_아이디_목록으로_팔로잉을_조회한다() {
        // given
        Following following1 = Following.builder()
                .followerId(1L)
                .followeeId(2L)
                .build();
        Following following2 = Following.builder()
                .followerId(1L)
                .followeeId(3L)
                .build();
        followingRepository.saveAll(List.of(following1, following2));

        // when
        List<Following> followings = followingRepository.findByFollowerIdAndFolloweeIdIn(1L, List.of(2L));

        // then
        assertThat(followings).hasSize(1)
                .containsExactly(following1);
    }
}
