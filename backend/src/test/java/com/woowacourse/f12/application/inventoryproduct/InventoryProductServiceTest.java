package com.woowacourse.f12.application.inventoryproduct;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.request.inventoryproduct.ProfileProductRequest;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductResponse;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductsResponse;
import com.woowacourse.f12.exception.badrequest.DuplicatedProfileProductCategoryException;
import com.woowacourse.f12.exception.badrequest.InvalidProfileProductCategoryException;
import com.woowacourse.f12.exception.badrequest.InvalidProfileProductUpdateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.woowacourse.f12.support.fixture.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.fixture.InventoryProductFixtures.UNSELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class InventoryProductServiceTest {

    @Mock
    private InventoryProductRepository inventoryProductRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private InventoryProductService inventoryProductService;

    @Test
    void 대표_장비를_수정한다() {
        // given
        List<Long> selectedInventoryProductIds = List.of(2L);
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(selectedInventoryProductIds);
        InventoryProduct inventoryProduct = UNSELECTED_INVENTORY_PRODUCT.생성(2L, null, KEYBOARD_1.생성(1L));
        Member member = CORINNE.인벤토리를_추가해서_생성(1L, List.of(inventoryProduct));
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));
        given(inventoryProductRepository.findAllById(selectedInventoryProductIds))
                .willReturn(List.of(inventoryProduct));

        // when
        inventoryProductService.updateProfileProducts(1L, profileProductRequest);

        // then
        assertAll(
                () -> verify(memberRepository).findById(1L),
                () -> verify(inventoryProductRepository).updateBulkProfileProductByMember(member, false),
                () -> verify(inventoryProductRepository).updateBulkProfileProductByMemberAndIds(member,
                        selectedInventoryProductIds, true)
        );
    }

    @Test
    void 등록하려는_대표장비가_인벤토리에_존재하지않으면_예외를_반환한다() {
        // given
        List<Long> selectedInventoryProductIds = List.of(2L);
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(selectedInventoryProductIds);
        Member member = CORINNE.생성(1L);
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));
        given(inventoryProductRepository.findAllById(selectedInventoryProductIds))
                .willReturn(List.of(UNSELECTED_INVENTORY_PRODUCT.생성(2L, member, KEYBOARD_1.생성(1L))));

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> inventoryProductService.updateProfileProducts(1L, profileProductRequest))
                        .isExactlyInstanceOf(InvalidProfileProductUpdateException.class),
                () -> verify(memberRepository).findById(1L),
                () -> verify(inventoryProductRepository).findAllById(selectedInventoryProductIds),
                () -> verify(inventoryProductRepository, times(0)).updateBulkProfileProductByMember(member, false),
                () -> verify(inventoryProductRepository, times(0)).updateBulkProfileProductByMemberAndIds(member,
                        selectedInventoryProductIds, true)
        );
    }

    @Test
    void 대표장비_변경_요청에_카테고리가_중복되면_예외를_반환한다() {
        // given
        List<Long> selectedInventoryProductIds = List.of(1L, 2L);
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(selectedInventoryProductIds);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, null, KEYBOARD_1.생성());
        InventoryProduct duplicatedCategoryInventoryProduct = UNSELECTED_INVENTORY_PRODUCT.생성(2L, null,
                KEYBOARD_2.생성());
        Member member = CORINNE.인벤토리를_추가해서_생성(1L, List.of(inventoryProduct, duplicatedCategoryInventoryProduct));
        duplicatedCategoryInventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(2L, null, KEYBOARD_2.생성());

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));
        given(inventoryProductRepository.findAllById(selectedInventoryProductIds))
                .willReturn(List.of(inventoryProduct, duplicatedCategoryInventoryProduct));

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> inventoryProductService.updateProfileProducts(1L, profileProductRequest))
                        .isExactlyInstanceOf(DuplicatedProfileProductCategoryException.class),
                () -> verify(memberRepository).findById(1L),
                () -> verify(inventoryProductRepository).findAllById(selectedInventoryProductIds)
        );
    }

    @Test
    void 대표장비_변경_요청에_소프트웨어_카테고리가_포함되면_예외를_반환한다() {
        // given
        List<Long> selectedInventoryProductIds = List.of(1L, 2L);
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(selectedInventoryProductIds);
        InventoryProduct selectedInventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(2L, null, KEYBOARD_1.생성(1L));
        Member member = CORINNE.인벤토리를_추가해서_생성(1L, List.of(selectedInventoryProduct));
        InventoryProduct inventoryProduct1 = SELECTED_INVENTORY_PRODUCT.생성(1L, member, SOFTWARE_1.생성());
        InventoryProduct inventoryProduct2 = UNSELECTED_INVENTORY_PRODUCT.생성(2L, member, KEYBOARD_2.생성());

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));
        given(inventoryProductRepository.findAllById(selectedInventoryProductIds))
                .willReturn(List.of(inventoryProduct1, inventoryProduct2));

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> inventoryProductService.updateProfileProducts(1L, profileProductRequest))
                        .isExactlyInstanceOf(InvalidProfileProductCategoryException.class),
                () -> verify(memberRepository).findById(1L),
                () -> verify(inventoryProductRepository).findAllById(selectedInventoryProductIds)
        );
    }

    @Test
    void 등록된_장비를_멤버_id로_조회한다() {
        // given
        Long memberId = 1L;
        Member member = CORINNE.생성(memberId);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, member, KEYBOARD_1.생성(1L));
        given(memberRepository.existsById(1L))
                .willReturn(true);
        given(inventoryProductRepository.findWithProductByMemberId(memberId))
                .willReturn(List.of(inventoryProduct));

        // when
        InventoryProductsResponse inventoryProductsResponse = inventoryProductService.findByMemberId(memberId);

        // then
        assertAll(
                () -> assertThat(inventoryProductsResponse.getItems()).hasSize(1)
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(InventoryProductResponse.from(inventoryProduct)),
                () -> verify(memberRepository).existsById(memberId),
                () -> verify(inventoryProductRepository).findWithProductByMemberId(memberId)
        );
    }
}
