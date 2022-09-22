package com.woowacourse.f12.domain.member;

import com.woowacourse.f12.config.JpaConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.FRONTEND;
import static com.woowacourse.f12.support.fixture.MemberFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import({JpaConfig.class})
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FollowingRepository followingRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void 회원_조회_시_팔로워_수를_함께_조회한다() {
        // given
        Member corinne = CORINNE.생성();
        Member mincho = MINCHO.생성();
        memberRepository.saveAll(List.of(corinne, mincho));
        followingRepository.save(Following.builder()
                .followerId(corinne.getId())
                .followingId(mincho.getId())
                .build());
        Member updateMember = Member.builder()
                .followerCount(mincho.getFollowerCount() + 1)
                .build();
        mincho.update(updateMember);
        entityManager.flush();
        entityManager.clear();

        // when
        Member savedMincho = memberRepository.findById(mincho.getId())
                .get();

        // then
        assertThat(savedMincho.getFollowerCount()).isOne();
    }

    @Test
    void 키워드와_옵션을_입력하지않고_회원을_조회한다() {
        // given
        memberRepository.saveAll(List.of(CORINNE.생성(), MINCHO.생성()));

        // when
        Slice<Member> slice = memberRepository.findBySearchConditions(null, null, null, PageRequest.of(0, 2));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsOnly(CORINNE.생성(), MINCHO.생성())
        );
    }

    @Test
    void 회원의_깃허브_아이디를_키워드로_조회한다() {
        // given
        memberRepository.saveAll(List.of(CORINNE.생성(), MINCHO.생성()));

        // when
        Slice<Member> slice = memberRepository.findBySearchConditions("hamcheeseburger", null, null,
                PageRequest.of(0, 2));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsOnly(CORINNE.생성())
        );
    }

    @Test
    void 회원의_깃허브_아이디_일부를_키워드로_조회한다() {
        // given
        memberRepository.saveAll(List.of(CORINNE.생성(), MINCHO.생성()));

        // when
        Slice<Member> slice = memberRepository.findBySearchConditions("cheese", null, null, PageRequest.of(0, 2));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsOnly(CORINNE.생성())
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"cheese"})
    void 회원을_키워드와_연차_옵션으로_조회한다(String keyword) {
        // given
        memberRepository.saveAll(List.of(CORINNE.생성(), MINCHO.생성()));

        // when
        Slice<Member> slice = memberRepository.findBySearchConditions(keyword, SENIOR, null,
                PageRequest.of(0, 2));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsOnly(CORINNE.생성())
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"cheese"})
    void 회원을_키워드와_연차와_직군_옵션으로_조회한다(String keyword) {
        // given
        memberRepository.saveAll(List.of(CORINNE.생성(), MINCHO.생성()));

        // when
        Slice<Member> slice = memberRepository.findBySearchConditions(keyword, SENIOR, BACKEND,
                PageRequest.of(0, 2));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsOnly(CORINNE.생성())
        );
    }

    @Test
    void 팔로잉하는_회원의_목록을_키워드와_옵션으로_조회한다() {
        // given
        Member corinne = CORINNE.생성();
        Member mincho = MINCHO.생성();
        Member ohzzi = OHZZI.생성();
        memberRepository.saveAll(List.of(corinne, mincho, ohzzi));
        followingRepository.save(Following.builder()
                .followerId(corinne.getId())
                .followingId(mincho.getId())
                .build());
        Member updateMember = Member.builder()
                        .followerCount(mincho.getFollowerCount() + 1)
                                .build();
        mincho.update(updateMember);
        entityManager.flush();
        entityManager.clear();

        Member expected = Member.builder()
                .id(mincho.getId())
                .gitHubId(mincho.getGitHubId())
                .name(mincho.getName())
                .imageUrl(mincho.getImageUrl())
                .careerLevel(mincho.getCareerLevel())
                .jobType(mincho.getJobType())
                .followerCount(1)
                .build();

        // when
        Slice<Member> slice = memberRepository.findFollowingsBySearchConditions(corinne.getId(), "jswith", JUNIOR,
                FRONTEND, PageRequest.of(0, 1, Sort.by("id").descending()));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).usingRecursiveFieldByFieldElementComparator()
                        .containsExactly(expected)
        );
    }

    @Test
    void 추가정보가_입력되지않은_회원은_회원을_조회할때_포함되지_않는다() {
        // given
        memberRepository.save(NOT_ADDITIONAL_INFO.생성());

        // when
        Slice<Member> slice = memberRepository.findBySearchConditions(null, null, null, PageRequest.of(0, 2));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).isEmpty()
        );
    }
}
