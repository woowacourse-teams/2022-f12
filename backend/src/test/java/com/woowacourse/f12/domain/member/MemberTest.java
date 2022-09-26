package com.woowacourse.f12.domain.member;

import com.woowacourse.f12.exception.badrequest.InvalidFollowerCountException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @Test
    void 회원_추가정보들을_수정한다() {
        // given
        Member member = Member.builder()
                .id(1L)
                .name("괴물")
                .imageUrl("imageUrl")
                .careerLevel(CareerLevel.JUNIOR)
                .jobType(JobType.ETC)
                .build();
        Member updateMember = Member.builder()
                .id(1L)
                .name("유현지")
                .imageUrl("updateImageUrl")
                .careerLevel(CareerLevel.SENIOR)
                .jobType(JobType.BACKEND)
                .build();
        // when
        member.update(updateMember);

        // then
        assertThat(member).usingRecursiveComparison()
                .isEqualTo(updateMember);
    }

    @Test
    void null인_필드는_수정하지_않는다() {
        Member member = Member.builder()
                .id(1L)
                .name("유현지")
                .imageUrl("imageUrl")
                .careerLevel(CareerLevel.SENIOR)
                .jobType(JobType.BACKEND)
                .build();
        Member expected = Member.builder()
                .id(1L)
                .name("유현지")
                .imageUrl("imageUrl")
                .careerLevel(CareerLevel.SENIOR)
                .jobType(JobType.BACKEND)
                .build();

        // when
        member.update(Member.builder().build());

        // then
        assertThat(member).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 팔로워_수를_증가한다() {
        // given
        Member member = Member.builder()
                .id(1L)
                .name("유현지")
                .imageUrl("imageUrl")
                .careerLevel(CareerLevel.SENIOR)
                .jobType(JobType.BACKEND)
                .build();
        Member expected = Member.builder()
                .id(1L)
                .name("유현지")
                .imageUrl("imageUrl")
                .careerLevel(CareerLevel.SENIOR)
                .jobType(JobType.BACKEND)
                .followerCount(1)
                .build();

        // when
        member.follow();

        // then
        assertThat(member).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 팔로워_수를_감소한다() {
        // given
        Member member = Member.builder()
                .id(1L)
                .name("유현지")
                .imageUrl("imageUrl")
                .careerLevel(CareerLevel.SENIOR)
                .jobType(JobType.BACKEND)
                .followerCount(1)
                .build();
        Member expected = Member.builder()
                .id(1L)
                .name("유현지")
                .imageUrl("imageUrl")
                .careerLevel(CareerLevel.SENIOR)
                .jobType(JobType.BACKEND)
                .build();

        // when
        member.unfollow();

        // then
        assertThat(member).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 팔로워_수가_0인_상태에서_팔로워_수를_감소하면_예외가_발생한다() {
        // given
        Member member = Member.builder()
                .id(1L)
                .name("유현지")
                .imageUrl("imageUrl")
                .careerLevel(CareerLevel.SENIOR)
                .jobType(JobType.BACKEND)
                .build();

        // when, then
        assertThatThrownBy(member::unfollow)
                .isInstanceOf(InvalidFollowerCountException.class);
    }
}
