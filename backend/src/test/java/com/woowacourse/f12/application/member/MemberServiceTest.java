package com.woowacourse.f12.application.member;

import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.dto.CareerLevelConstant.JUNIOR_CONSTANT;
import static com.woowacourse.f12.dto.CareerLevelConstant.SENIOR_CONSTANT;
import static com.woowacourse.f12.dto.JobTypeConstant.BACKEND_CONSTANT;
import static com.woowacourse.f12.dto.JobTypeConstant.ETC_CONSTANT;
import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.request.member.MemberSearchRequest;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.dto.response.member.MemberWithProfileProductResponse;
import com.woowacourse.f12.exception.badrequest.InvalidProfileArgumentException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

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
        memberService.updateMember(1L, new MemberRequest(JUNIOR_CONSTANT, ETC_CONSTANT));

        // then
        verify(memberRepository).findById(1L);
    }

    @Test
    void 키워드와_옵션으로_회원을_조회한다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(CORINNE.생성(1L), KEYBOARD_1.생성(1L));
        Member member = CORINNE.대표장비를_추가해서_생성(1L, inventoryProduct);

        given(memberRepository.findByContains("cheese", SENIOR, BACKEND, pageable))
                .willReturn(new SliceImpl<>(List.of(member), pageable, false));

        // when
        MemberSearchRequest memberSearchRequest = new MemberSearchRequest("cheese", SENIOR_CONSTANT, BACKEND_CONSTANT);
        MemberPageResponse memberPageResponse = memberService.findByContains(memberSearchRequest, pageable);

        // then
        assertAll(
                () -> verify(memberRepository).findByContains("cheese", SENIOR, BACKEND, pageable),
                () -> assertThat(memberPageResponse.isHasNext()).isFalse(),
                () -> assertThat(memberPageResponse.getItems()).usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(MemberWithProfileProductResponse.from(member))
        );
    }

}
