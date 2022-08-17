package com.woowacourse.f12.domain.member;

import com.woowacourse.f12.config.JpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

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
}
