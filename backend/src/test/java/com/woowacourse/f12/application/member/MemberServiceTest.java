package com.woowacourse.f12.application.member;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.JobType.ETC;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.exception.badrequest.InvalidProfileArgumentException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void 멤버_아이디로_회원정보를_조회한다() {
        // given
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(CORINNE.생성(1L)));

        // when
        MemberResponse memberResponse = memberService.findById(1L);

        // then
        assertAll(
                () -> assertThat(memberResponse).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(CORINNE.생성(1L))),
                () -> verify(memberRepository).findById(1L)
        );
    }

    @Test
    void 존재_하지않는_멤버_아이디로_회원정보를_조회하면_예외가_발생한다() {
        // given
        given(memberRepository.findById(1L))
                .willThrow(new MemberNotFoundException());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.findById(1L))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).findById(1L)
        );
    }

    @Test
    void 추가_정보가_입력되지_않은_멤버_아이디로_회원정보를_조회하면_예외가_발생한다() {
        // given
        Member member = Member.builder()
                .id(1L)
                .gitHubId("gitHubId")
                .name("name")
                .imageUrl("url")
                .build();
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.findById(1L))
                        .isExactlyInstanceOf(InvalidProfileArgumentException.class),
                () -> verify(memberRepository).findById(1L)
        );
    }

    @Test
    void 회원정보를_업데이트_한다() {
        // given
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(CORINNE.생성(1L)));

        // when
        memberService.updateMember(1L, new MemberRequest(JUNIOR, ETC));

        // then
        verify(memberRepository).findById(1L);
    }
}
