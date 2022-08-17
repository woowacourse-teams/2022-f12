package com.woowacourse.f12.application.member;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.Following;
import com.woowacourse.f12.domain.member.FollowingRepository;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.request.member.MemberSearchRequest;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.dto.response.member.MemberWithProfileProductResponse;
import com.woowacourse.f12.exception.badrequest.AlreadyFollowingException;
import com.woowacourse.f12.exception.badrequest.NotFollowingException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import com.woowacourse.f12.support.MemberFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.ETC_CONSTANT;
import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FollowingRepository followingRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void 비회원이_멤버_아이디로_회원정보를_조회한다() {
        // given
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(CORINNE.생성(1L)));

        // when
        MemberResponse memberResponse = memberService.findById(1L, null);

        // then
        assertAll(
                () -> assertThat(memberResponse).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(CORINNE.생성(1L), false)),
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
        given(followingRepository.existsByFollowerIdAndFolloweeId(loggedInId, targetId))
                .willReturn(false);

        // when
        MemberResponse actual = memberService.findById(targetId, loggedInId);

        // then
        assertAll(
                () -> assertThat(actual).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(corinne, false)),
                () -> verify(memberRepository).findById(targetId),
                () -> verify(followingRepository).existsByFollowerIdAndFolloweeId(loggedInId, targetId)
        );
    }

    @Test
    void 존재_하지않는_멤버_아이디로_회원정보를_조회하면_예외가_발생한다() {
        // given
        given(memberRepository.findById(1L))
                .willThrow(new MemberNotFoundException());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.findById(1L, null))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).findById(1L)
        );
    }

    @Test
    void 추가_정보가_입력되지_않은_멤버_아이디로_회원정보를_조회한다() {
        // given
        Member member = MemberFixtures.NOT_ADDITIONAL_INFO.생성();
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));

        // when
        MemberResponse memberResponse = memberService.findById(1L, null);
        // then
        assertAll(
                () -> assertThat(memberResponse).usingRecursiveComparison()
                        .isEqualTo(MemberResponse.from(member, false)),
                () -> verify(memberRepository).findById(1L)
        );
    }

    @Test
    void 회원정보를_업데이트_한다() {
        // given
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(CORINNE.생성(1L)));

        // when
        memberService.updateMember(1L, new MemberRequest(JUNIOR_CONSTANT, ETC_CONSTANT));

        // then
        verify(memberRepository).findById(1L);
    }

    @Test
    void 키워드와_옵션으로_회원을_조회한다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L), KEYBOARD_1.생성(1L));
        Member member = CORINNE.인벤토리를_추가해서_생성(1L, inventoryProduct);

        given(memberRepository.findBySearchConditions("cheese", SENIOR, BACKEND, pageable))
                .willReturn(new SliceImpl<>(List.of(member), pageable, false));

        // when
        MemberSearchRequest memberSearchRequest = new MemberSearchRequest("cheese", SENIOR_CONSTANT, BACKEND_CONSTANT);
        MemberPageResponse memberPageResponse = memberService.findByContains(memberSearchRequest, pageable);

        // then
        assertAll(
                () -> verify(memberRepository).findBySearchConditions("cheese", SENIOR, BACKEND, pageable),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(MemberWithProfileProductResponse.from(member))
        );
    }

    @Test
    void 다른_회원을_팔로우한다() {
        // given
        Long followerId = 1L;
        Long followeeId = 2L;
        Following following = Following.builder()
                .followerId(followerId)
                .followeeId(followeeId)
                .build();

        given(memberRepository.existsById(followerId))
                .willReturn(true);
        given(memberRepository.existsById(followeeId))
                .willReturn(true);
        given(followingRepository.save(following)).willReturn(following);

        // when
        memberService.follow(followerId, followeeId);

        // then
        assertAll(
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository).existsById(followeeId),
                () -> verify(followingRepository).existsByFollowerIdAndFolloweeId(followerId, followeeId),
                () -> verify(followingRepository).save(following)
        );
    }

    @Test
    void 다른_회원을_팔로우할때_팔로워가_존재하지_않으면_예외를_반환한다() {
        // given
        Long followerId = 1L;
        Long followeeId = 2L;
        given(memberRepository.existsById(followerId))
                .willReturn(false);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.follow(followerId, followeeId))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository, times(0)).existsById(followeeId),
                () -> verify(followingRepository, times(0)).existsByFollowerIdAndFolloweeId(followerId, followeeId),
                () -> verify(followingRepository, times(0)).save(any(Following.class))
        );
    }

    @Test
    void 다른_회원을_팔로우할때_팔로이가_존재하지_않으면_예외를_반환한다() {
        // given
        Long followerId = 1L;
        Long followeeId = 2L;
        given(memberRepository.existsById(followerId))
                .willReturn(true);
        given(memberRepository.existsById(followeeId))
                .willReturn(false);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.follow(followerId, followeeId))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository).existsById(followeeId),
                () -> verify(followingRepository, times(0)).existsByFollowerIdAndFolloweeId(followerId, followeeId),
                () -> verify(followingRepository, times(0)).save(any(Following.class))
        );
    }

    @Test
    void 이미_팔로우하고_있는_회원을_팔로우하려_하면_예외를_반환한다() {
        // given
        Long followerId = 1L;
        Long followeeId = 2L;
        given(memberRepository.existsById(followerId))
                .willReturn(true);
        given(memberRepository.existsById(followeeId))
                .willReturn(true);
        given(followingRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId))
                .willReturn(true);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.follow(followerId, followeeId))
                        .isExactlyInstanceOf(AlreadyFollowingException.class),
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository).existsById(followeeId),
                () -> verify(followingRepository).existsByFollowerIdAndFolloweeId(followerId, followeeId),
                () -> verify(followingRepository, times(0)).save(any(Following.class))
        );
    }

    @Test
    void 다른_회원을_언팔로우한다() {
        // given
        Long followerId = 1L;
        Long followeeId = 2L;
        Following following = Following.builder()
                .followerId(followerId)
                .followeeId(followeeId)
                .build();

        given(memberRepository.existsById(followerId))
                .willReturn(true);
        given(memberRepository.existsById(followeeId))
                .willReturn(true);
        given(followingRepository.findByFollowerIdAndFolloweeId(followerId, followeeId))
                .willReturn(Optional.of(following));
        willDoNothing().given(followingRepository)
                .delete(following);

        // when
        memberService.unfollow(followerId, followeeId);

        // then
        assertAll(
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository).existsById(followeeId),
                () -> verify(followingRepository).findByFollowerIdAndFolloweeId(followerId, followeeId),
                () -> verify(followingRepository).delete(following)
        );
    }

    @Test
    void 다른_회원을_언팔로우할_때_팔로워가_존재하지_않으면_예외를_반환한다() {
        // given
        Long followerId = 1L;
        Long followeeId = 2L;
        Following following = Following.builder()
                .followerId(followerId)
                .followeeId(followeeId)
                .build();

        given(memberRepository.existsById(followerId))
                .willReturn(false);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.unfollow(followerId, followeeId))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository, times(0)).existsById(followeeId),
                () -> verify(followingRepository, times(0)).findByFollowerIdAndFolloweeId(followerId, followeeId),
                () -> verify(followingRepository, times(0)).delete(following)
        );
    }

    @Test
    void 다른_회원을_언팔로우할_때_팔로이가_존재하지_않으면_예외를_반환한다() {
        // given
        Long followerId = 1L;
        Long followeeId = 2L;
        Following following = Following.builder()
                .followerId(followerId)
                .followeeId(followeeId)
                .build();

        given(memberRepository.existsById(followerId))
                .willReturn(true);
        given(memberRepository.existsById(followeeId))
                .willReturn(false);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.unfollow(followerId, followeeId))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository).existsById(followeeId),
                () -> verify(followingRepository, times(0)).findByFollowerIdAndFolloweeId(followerId, followeeId),
                () -> verify(followingRepository, times(0)).delete(following)
        );
    }

    @Test
    void 다른_회원을_언팔로우할_때_팔로잉_상태가_아니면_예외를_반환한다() {
        // given
        Long followerId = 1L;
        Long followeeId = 2L;
        Following following = Following.builder()
                .followerId(followerId)
                .followeeId(followeeId)
                .build();

        given(memberRepository.existsById(followerId))
                .willReturn(true);
        given(memberRepository.existsById(followeeId))
                .willReturn(true);
        given(followingRepository.findByFollowerIdAndFolloweeId(followerId, followeeId))
                .willReturn(Optional.empty());

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.unfollow(followerId, followeeId))
                        .isExactlyInstanceOf(NotFollowingException.class),
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository).existsById(followeeId),
                () -> verify(followingRepository).findByFollowerIdAndFolloweeId(followerId, followeeId),
                () -> verify(followingRepository, times(0)).delete(following)
        );
    }
}
