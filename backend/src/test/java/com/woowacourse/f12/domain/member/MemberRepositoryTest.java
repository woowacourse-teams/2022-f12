package com.woowacourse.f12.domain.member;

import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static com.woowacourse.f12.support.MemberFixtures.MINCHO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.config.JpaConfig;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@DataJpaTest
@Import({JpaConfig.class})
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 키워드와_옵션을_입력하지않고_회원을_조회한다() {
        // given
        memberRepository.saveAll(List.of(CORINNE.생성(), MINCHO.생성()));

        // when
        Slice<Member> slice = memberRepository.findByContains(null, null, null, PageRequest.of(0, 2));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .contains(CORINNE.생성(), MINCHO.생성())
        );
    }

    @Test
    void 회원의_깃허브_아이디를_키워드로_조회한다() {
        // given
        memberRepository.saveAll(List.of(CORINNE.생성(), MINCHO.생성()));

        // when
        Slice<Member> slice = memberRepository.findByContains("hamcheeseburger", null, null, PageRequest.of(0, 2));

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
        Slice<Member> slice = memberRepository.findByContains("cheese", null, null, PageRequest.of(0, 2));

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
    void 회원의_연차를_옵션으로_조회한다(String keyword) {
        // given
        memberRepository.saveAll(List.of(CORINNE.생성(), MINCHO.생성()));

        // when
        Slice<Member> slice = memberRepository.findByContains(keyword, SENIOR, null, PageRequest.of(0, 2));

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
    void 회원의_직군을_옵션으로_조회한다(String keyword) {
        // given
        memberRepository.saveAll(List.of(CORINNE.생성(), MINCHO.생성()));

        // when
        Slice<Member> slice = memberRepository.findByContains(keyword, SENIOR, BACKEND,
                PageRequest.of(0, 2));

        // then
        assertAll(
                () -> assertThat(slice.hasNext()).isFalse(),
                () -> assertThat(slice.getContent()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .containsOnly(CORINNE.생성())
        );
    }
}
