package com.woowacourse.f12.application.member;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.presentation.member.JobTypeConstant.ETC_CONSTANT;
import static com.woowacourse.f12.support.fixture.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.MemberFixture.NOT_ADDITIONAL_INFO;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
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
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(CORINNE.생성(1L)));

        // when
        memberService.updateMember(1L, new MemberRequest(JUNIOR_CONSTANT, ETC_CONSTANT));

        // then
        verify(memberRepository).findById(1L);
    }

    @Test
    void 비회원이_키워드와_옵션으로_회원을_조회한다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L), KEYBOARD_1.생성(1L));
        Member member = CORINNE.인벤토리를_추가해서_생성(1L, List.of(inventoryProduct));

        given(memberRepository.findBySearchConditions("cheese", SENIOR, BACKEND, pageable))
                .willReturn(new SliceImpl<>(List.of(member), pageable, false));
        given(inventoryProductRepository.findWithProductByMembers(List.of(member)))
                .willReturn(List.of(inventoryProduct));

        // when
        MemberSearchRequest memberSearchRequest = new MemberSearchRequest("cheese", SENIOR_CONSTANT, BACKEND_CONSTANT);
        MemberPageResponse memberPageResponse = memberService.findByContains(null, memberSearchRequest, pageable);

        // then
        assertAll(
                () -> verify(memberRepository).findBySearchConditions("cheese", SENIOR, BACKEND, pageable),
                () -> verify(inventoryProductRepository).findWithProductByMembers(List.of(member)),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(MemberWithProfileProductResponse.of(member, false))
        );
    }

    @Test
    void 회원이_키워드와_옵션으로_회원을_조회한다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L), KEYBOARD_1.생성(1L));
        Member member = CORINNE.인벤토리를_추가해서_생성(1L, List.of(inventoryProduct));

        Long loggedInId = 2L;
        Following following = Following.builder()
                .followerId(loggedInId)
                .followingId(member.getId())
                .build();

        given(memberRepository.findBySearchConditions("cheese", SENIOR, BACKEND, pageable))
                .willReturn(new SliceImpl<>(List.of(member), pageable, false));
        given(inventoryProductRepository.findWithProductByMembers(List.of(member)))
                .willReturn(List.of(inventoryProduct));
        given(followingRepository.findByFollowerIdAndFollowingIdIn(loggedInId, List.of(member.getId())))
                .willReturn(List.of(following));

        // when
        MemberSearchRequest memberSearchRequest = new MemberSearchRequest("cheese", SENIOR_CONSTANT, BACKEND_CONSTANT);
        MemberPageResponse memberPageResponse = memberService.findByContains(loggedInId, memberSearchRequest, pageable);

        // then
        assertAll(
                () -> verify(memberRepository).findBySearchConditions("cheese", SENIOR, BACKEND, pageable),
                () -> verify(inventoryProductRepository).findWithProductByMembers(List.of(member)),
                () -> verify(followingRepository).findByFollowerIdAndFollowingIdIn(loggedInId, List.of(member.getId())),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(MemberWithProfileProductResponse.of(member, true))
        );
    }

    @Test
    void 다른_회원을_팔로우한다() {
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
                () -> verify(followingRepository).save(following)
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
                () -> verify(followingRepository, times(0)).save(any(Following.class))
        );
    }

    @Test
    void 다른_회원을_팔로우할때_팔로이가_존재하지_않으면_예외를_반환한다() {
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
                () -> verify(followingRepository, times(0)).save(any(Following.class))
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
                () -> verify(followingRepository, times(0)).save(any(Following.class))
        );
    }

    @Test
    void 다른_회원을_언팔로우한다() {
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
                () -> verify(followingRepository).delete(following)
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
                () -> verify(followingRepository, times(0)).delete(following)
        );
    }

    @Test
    void 다른_회원을_언팔로우할_때_팔로이가_존재하지_않으면_예외를_반환한다() {
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
                .willReturn(false);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.unfollow(followerId, followingId))
                        .isExactlyInstanceOf(MemberNotFoundException.class),
                () -> verify(memberRepository).existsById(followerId),
                () -> verify(memberRepository).existsById(followingId),
                () -> verify(followingRepository, times(0))
                        .findByFollowerIdAndFollowingId(followerId, followingId),
                () -> verify(followingRepository, times(0)).delete(following)
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

    @Test
    void 팔로잉하는_회원을_조회한다() {
        // given
        Long loggedInId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Member member = CORINNE.생성(2L);
        MemberSearchRequest memberSearchRequest = new MemberSearchRequest(null, null, null);

        given(memberRepository.findFollowingsBySearchConditions(loggedInId, null, null, null, pageable))
                .willReturn(new SliceImpl<>(List.of(member), pageable, false));
        given(inventoryProductRepository.findWithProductByMembers(List.of(member)))
                .willReturn(Collections.emptyList());

        // when
        MemberPageResponse memberPageResponse = memberService.findFollowingsByConditions(loggedInId,
                memberSearchRequest, pageable);

        // then
        assertAll(
                () -> verify(memberRepository).findFollowingsBySearchConditions(loggedInId, null, null, null, pageable),
                () -> verify(inventoryProductRepository).findWithProductByMembers(List.of(member)),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(MemberWithProfileProductResponse.of(member, true))
        );
    }
}
