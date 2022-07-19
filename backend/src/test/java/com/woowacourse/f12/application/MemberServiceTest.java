package com.woowacourse.f12.application;

import static com.woowacourse.f12.domain.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.JobType.ETC;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import com.woowacourse.f12.domain.MemberRepository;
import com.woowacourse.f12.dto.request.MemberRequest;
import com.woowacourse.f12.dto.response.MemberResponse;
import com.woowacourse.f12.exception.MemberNotFoundException;
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
