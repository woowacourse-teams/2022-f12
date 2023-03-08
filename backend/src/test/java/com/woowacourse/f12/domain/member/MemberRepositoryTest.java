package com.woowacourse.f12.domain.member;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.FRONTEND;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.MemberFixture.MINCHO;
import static com.woowacourse.f12.support.fixture.MemberFixture.NOT_ADDITIONAL_INFO;
import static com.woowacourse.f12.support.fixture.MemberFixture.OHZZI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.RepositoryTest;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

@RepositoryTest
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
        memberRepository.increaseFollowerCount(mincho.getId());

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
        Slice<Member> slice = memberRepository.findWithOutSearchConditions(PageRequest.of(0, 2));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsOnly(CORINNE.생성(), MINCHO.생성())
        );
    }

    @Test
    void 옵션만_입력하고_회원을_조회한다() {
        // given
        memberRepository.saveAll(List.of(CORINNE.생성(), MINCHO.생성()));

        // when
        Slice<Member> slice = memberRepository.findWithSearchConditions(null, SENIOR, null, PageRequest.of(0, 2));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsOnly(CORINNE.생성())
        );
    }

    @Test
    void 회원의_깃허브_아이디를_키워드로_조회한다() {
        // given
        memberRepository.saveAll(List.of(CORINNE.생성(), MINCHO.생성()));

        // when
        Slice<Member> slice = memberRepository.findWithSearchConditions("hamcheeseburger", null, null,
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
        Slice<Member> slice = memberRepository.findWithSearchConditions("cheese", null, null, PageRequest.of(0, 2));

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
        Slice<Member> slice = memberRepository.findWithSearchConditions(keyword, SENIOR, null,
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
        Slice<Member> slice = memberRepository.findWithSearchConditions(keyword, SENIOR, BACKEND,
                PageRequest.of(0, 2));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsOnly(CORINNE.생성())
        );
    }

    @Test
    void 팔로잉하는_회원의_목록을_키워드와_옵션을_입력하지않고_조회한다() {
        // given
        Member corinne = CORINNE.생성();
        Member mincho = MINCHO.생성();
        Member ohzzi = OHZZI.생성();
        memberRepository.saveAll(List.of(corinne, mincho, ohzzi));
        followingRepository.save(Following.builder()
                .followerId(corinne.getId())
                .followingId(mincho.getId())
                .build());
        memberRepository.increaseFollowerCount(mincho.getId());

        Member expected = Member.builder()
                .id(mincho.getId())
                .gitHubId(mincho.getGitHubId())
                .name(mincho.getName())
                .imageUrl(mincho.getImageUrl())
                .registered(true)
                .careerLevel(mincho.getCareerLevel())
                .jobType(mincho.getJobType())
                .followerCount(1)
                .build();

        // when
        Slice<Member> slice = memberRepository.findFollowingsWithoutSearchConditions(corinne.getId(),
                PageRequest.of(0, 1, Sort.by("id").descending()));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).usingRecursiveFieldByFieldElementComparator()
                        .containsExactly(expected)
        );
    }

    @Test
    void 팔로잉하는_회원의_목록을_옵션만으로_조회한다() {
        // given
        Member corinne = CORINNE.생성();
        Member mincho = MINCHO.생성();
        Member ohzzi = OHZZI.생성();
        memberRepository.saveAll(List.of(corinne, mincho, ohzzi));
        followingRepository.save(Following.builder()
                .followerId(corinne.getId())
                .followingId(mincho.getId())
                .build());
        memberRepository.increaseFollowerCount(mincho.getId());

        Member expected = Member.builder()
                .id(mincho.getId())
                .gitHubId(mincho.getGitHubId())
                .name(mincho.getName())
                .imageUrl(mincho.getImageUrl())
                .registered(true)
                .careerLevel(mincho.getCareerLevel())
                .jobType(mincho.getJobType())
                .followerCount(1)
                .build();

        // when
        Slice<Member> slice = memberRepository.findFollowingsWithSearchConditions(corinne.getId(), null, null, FRONTEND,
                PageRequest.of(0, 1, Sort.by("id").descending()));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).usingRecursiveFieldByFieldElementComparator()
                        .containsExactly(expected)
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
        memberRepository.increaseFollowerCount(mincho.getId());

        Member expected = Member.builder()
                .id(mincho.getId())
                .gitHubId(mincho.getGitHubId())
                .name(mincho.getName())
                .imageUrl(mincho.getImageUrl())
                .registered(true)
                .careerLevel(mincho.getCareerLevel())
                .jobType(mincho.getJobType())
                .followerCount(1)
                .build();

        // when
        Slice<Member> slice = memberRepository.findFollowingsWithSearchConditions(corinne.getId(), "jswith", JUNIOR,
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
        Slice<Member> slice = memberRepository.findWithSearchConditions(null, null, null, PageRequest.of(0, 2));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).isEmpty()
        );
    }

    @Test
    void 깃허브_이름은_회원간_중복일_수_없다() {
        // given
        Member member1 = Member.builder()
                .name("유현지")
                .gitHubId("hamcheeseburger")
                .imageUrl("imageUrl")
                .careerLevel(CareerLevel.SENIOR)
                .jobType(JobType.BACKEND)
                .build();
        Member member2 = Member.builder()
                .name("유현주")
                .gitHubId("hamcheeseburger")
                .imageUrl("imageUrl")
                .careerLevel(CareerLevel.SENIOR)
                .jobType(JobType.BACKEND)
                .build();
        memberRepository.save(member1);

        // when, then
        assertThatThrownBy(() -> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 팔로워_수를_증가시킨다() {
        // given
        Member member = CORINNE.생성();
        memberRepository.save(member);

        // when
        memberRepository.increaseFollowerCount(member.getId());

        // then
        Member actual = memberRepository.findById(member.getId())
                .orElseThrow();

        assertThat(actual.getFollowerCount()).isOne();
    }

    @Test
    void 팔로워_수를_감소시킨다() {
        // given
        Member member = CORINNE.생성();
        memberRepository.save(member);
        memberRepository.increaseFollowerCount(member.getId());

        // when
        memberRepository.decreaseFollowerCount(member.getId());

        // then
        Member actual = memberRepository.findById(member.getId())
                .orElseThrow();

        assertThat(actual.getFollowerCount()).isZero();
    }
}
