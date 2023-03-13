package com.woowacourse.f12.application.member;

import static com.woowacourse.f12.presentation.member.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.ETC_CONSTANT;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.MemberFixture.NOT_ADDITIONAL_INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.member.Following;
import com.woowacourse.f12.domain.member.FollowingRepository;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.exception.badrequest.AlreadyFollowingException;
import com.woowacourse.f12.exception.badrequest.NotFollowingException;
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

    @Mock
    private FollowingRepository followingRepository;

    @Mock
    private InventoryProductRepository inventoryProductRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void 비회원이_멤버_아이디로_회원정보를_조회한다() {
        // given
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(CORINNE.생성(1L)));

        // when
        MemberResponse memberResponse = memberService.find(1L, null);

        // then
        assertAll(
                () -> assertThat(memberResponse).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.of(CORINNE.생성(1L), false)),
                () -> verify(memberRepository).findById(1L)
        );
    }

    @Test
    void 로그인된_회원이_다른_회원의_정보를_아이디로_조회한다() {
        // given
        Long targetId = 1L;
        Long loggedInId = 2L;
        Member corinne = CORINNE.생성(targetId);

        given(memberRepository.findById(targetId))
                .willReturn(Optional.of(corinne));
        given(followingRepository.existsByFollowerIdAndFollowingId(loggedInId, targetId))
                .willReturn(false);

        // when
        MemberResponse actual = memberService.find(targetId, loggedInId);

        // then
        assertAll(
                () -> assertThat(actual).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.of(corinne, false)),
                () -> verify(memberRepository).findById(targetId),
                () -> verify(followingRepository).existsByFollowerIdAndFollowingId(loggedInId, targetId)
        );
    }

    @Test
    void 존재_하지않는_멤버_아이디로_회원정보를_조회하면_예외가_발생한다() {
        // given
        given(memberRepository.findById(1L))
                .willThrow(new MemberNotFoundException());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.find(1L, null))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).findById(1L)
        );
    }

    @Test
    void 추가_정보가_입력되지_않은_멤버_아이디로_회원정보를_조회한다() {
        // given
        Member member = NOT_ADDITIONAL_INFO.생성();
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));

        // when
        MemberResponse memberResponse = memberService.find(1L, null);
        // then
        assertAll(
                () -> assertThat(memberResponse).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.of(member, false)),
                () -> verify(memberRepository).findById(1L)
        );
    }

    @Test
    void 회원정보를_업데이트_한다() {
        // given
        final Member corinne = CORINNE.생성(1L);
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(CORINNE.생성(1L)));

        // when
        memberService.updateMember(1L, new MemberRequest(JUNIOR_CONSTANT, ETC_CONSTANT));

        // then
        assertAll(
                () -> verify(memberRepository).findById(1L),
                () -> assertThat(corinne.isRegistered()).isTrue()
        );
    }

    @Test
    void 다른_회원을_팔로우한다_팔로우에_성공하면_팔로잉_멤버의_팔로워_카운트가_증가하는_쿼리를_실행한다() {
        // given
        Long followerId = 1L;
        Long followingId = 2L;
        Following following = Following.builder()
                .followerId(followerId)
                .followingId(followingId)
                .build();

        given(memberRepository.existsById(followerId))
                .willReturn(true);
        given(memberRepository.existsById(followingId))
                .willReturn(true);
        given(followingRepository.save(following)).willReturn(following);

        // when
        memberService.follow(followerId, followingId);

        // then
        assertAll(
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository).existsById(followingId),
                () -> verify(followingRepository).existsByFollowerIdAndFollowingId(followerId, followingId),
                () -> verify(followingRepository).save(following),
                () -> verify(memberRepository).increaseFollowerCount(followingId)
        );
    }

    @Test
    void 다른_회원을_팔로우할때_팔로워가_존재하지_않으면_예외를_반환한다() {
        // given
        Long followerId = 1L;
        Long followingId = 2L;
        given(memberRepository.existsById(followerId))
                .willReturn(false);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.follow(followerId, followingId))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository, times(0)).existsById(followingId),
                () -> verify(followingRepository, times(0))
                        .existsByFollowerIdAndFollowingId(followerId, followingId),
                () -> verify(followingRepository, times(0)).save(any(Following.class)),
                () -> verify(memberRepository, times(0)).increaseFollowerCount(anyLong())
        );
    }

    @Test
    void 다른_회원을_팔로우할때_대상회원이_존재하지_않으면_예외를_반환한다() {
        // given
        Long followerId = 1L;
        Long followingId = 2L;

        given(memberRepository.existsById(followerId))
                .willReturn(true);
        given(memberRepository.existsById(followingId))
                .willReturn(false);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.follow(followerId, followingId))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository).existsById(followingId),
                () -> verify(followingRepository, times(0))
                        .existsByFollowerIdAndFollowingId(followerId, followingId),
                () -> verify(followingRepository, times(0)).save(any(Following.class)),
                () -> verify(memberRepository, times(0)).increaseFollowerCount(anyLong())
        );
    }

    @Test
    void 이미_팔로우하고_있는_회원을_팔로우하려_하면_예외를_반환한다() {
        // given
        Long followerId = 1L;
        Long followingId = 2L;
        given(memberRepository.existsById(followerId))
                .willReturn(true);
        given(memberRepository.existsById(followingId))
                .willReturn(true);
        given(followingRepository.existsByFollowerIdAndFollowingId(followerId, followingId))
                .willReturn(true);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.follow(followerId, followingId))
                        .isExactlyInstanceOf(AlreadyFollowingException.class),
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository).existsById(followingId),
                () -> verify(followingRepository).existsByFollowerIdAndFollowingId(followerId, followingId),
                () -> verify(followingRepository, times(0)).save(any(Following.class)),
                () -> verify(memberRepository, times(0)).increaseFollowerCount(anyLong())
        );
    }

    @Test
    void 다른_회원을_언팔로우한다_성공하면_팔로잉_멤버의_팔로워_카운트를_감소시키는_쿼리를_호출한다() {
        // given
        Long followerId = 1L;
        Long followingId = 2L;
        Following following = Following.builder()
                .followerId(followerId)
                .followingId(followingId)
                .build();

        given(memberRepository.existsById(followerId))
                .willReturn(true);
        given(memberRepository.existsById(followingId))
                .willReturn(true);
        given(followingRepository.findByFollowerIdAndFollowingId(followerId, followingId))
                .willReturn(Optional.of(following));
        willDoNothing().given(followingRepository)
                .delete(following);

        // when
        memberService.unfollow(followerId, followingId);

        // then
        assertAll(
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository).existsById(followingId),
                () -> verify(followingRepository).findByFollowerIdAndFollowingId(followerId, followingId),
                () -> verify(followingRepository).delete(following),
                () -> verify(memberRepository).decreaseFollowerCount(anyLong())
        );
    }

    @Test
    void 다른_회원을_언팔로우할_때_팔로워가_존재하지_않으면_예외를_반환한다() {
        // given
        Long followerId = 1L;
        Long followingId = 2L;
        Following following = Following.builder()
                .followerId(followerId)
                .followingId(followingId)
                .build();

        given(memberRepository.existsById(followerId))
                .willReturn(false);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.unfollow(followerId, followingId))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository, times(0)).existsById(followingId),
                () -> verify(followingRepository, times(0))
                        .findByFollowerIdAndFollowingId(followerId, followingId),
                () -> verify(followingRepository, times(0)).delete(following),
                () -> verify(memberRepository, times(0)).decreaseFollowerCount(anyLong())
        );
    }

    @Test
    void 다른_회원을_언팔로우할_때_팔로우_대상이_존재하지_않으면_예외를_반환한다() {
        // given
        Long followerId = 1L;
        Long followingId = 2L;

        given(memberRepository.existsById(followerId))
                .willReturn(true);
        given(memberRepository.existsById(followingId))
                .willReturn(false);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.unfollow(followerId, followingId))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository).existsById(followingId),
                () -> verify(followingRepository, times(0))
                        .findByFollowerIdAndFollowingId(followerId, followingId),
                () -> verify(followingRepository, times(0)).delete(any(Following.class)),
                () -> verify(memberRepository, times(0)).decreaseFollowerCount(anyLong())
        );
    }

    @Test
    void 다른_회원을_언팔로우할_때_팔로잉_상태가_아니면_예외를_반환한다() {
        // given
        Long followerId = 1L;
        Long followingId = 2L;
        Following following = Following.builder()
                .followerId(followerId)
                .followingId(followingId)
                .build();

        given(memberRepository.existsById(followerId))
                .willReturn(true);
        given(memberRepository.existsById(followingId))
                .willReturn(true);
        given(followingRepository.findByFollowerIdAndFollowingId(followerId, followingId))
                .willReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.unfollow(followerId, followingId))
                        .isExactlyInstanceOf(NotFollowingException.class),
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository).existsById(followingId),
                () -> verify(followingRepository).findByFollowerIdAndFollowingId(followerId, followingId),
                () -> verify(followingRepository, times(0)).delete(following)
        );
    }
}
