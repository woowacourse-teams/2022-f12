package com.woowacourse.f12.application.inventoryproduct;

import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.InventoryProductFixtures.UNSELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_2;
import static com.woowacourse.f12.support.ProductFixture.SOFTWARE_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.request.inventoryproduct.ProfileProductRequest;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductResponse;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductsResponse;
import com.woowacourse.f12.exception.badrequest.DuplicatedCategoryProfileProductException;
import com.woowacourse.f12.exception.badrequest.InvalidCategoryProfileProductException;
import com.woowacourse.f12.exception.internalserver.SqlUpdateException;
import com.woowacourse.f12.support.MemberFixtures;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        Member member = MemberFixtures.CORINNE.생성(1L);
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));
        given(inventoryProductRepository.updateBulkProfileProductByMember(member))
                .willReturn(1);
        given(inventoryProductRepository.updateBulkProfileProductByMemberAndIds(member, selectedInventoryProductIds))
                .willReturn(selectedInventoryProductIds.size());

        // when
        inventoryProductService.updateProfileProducts(1L, profileProductRequest);

        // then
        assertAll(
                () -> verify(memberRepository).findById(1L),
                () -> verify(inventoryProductRepository).updateBulkProfileProductByMember(member),
                () -> verify(inventoryProductRepository).updateBulkProfileProductByMemberAndIds(member,
                        selectedInventoryProductIds)
        );
    }

    @Test
    void 수정하려는_장비_갯수와_실제_등록된_대표_장비_갯수와_일치하지_않으면_예외를_반환한다() {
        // given
        List<Long> selectedInventoryProductIds = List.of(2L);
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(selectedInventoryProductIds);
        Member member = MemberFixtures.CORINNE.생성(1L);
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));
        given(inventoryProductRepository.updateBulkProfileProductByMember(member))
                .willReturn(1);
        given(inventoryProductRepository.updateBulkProfileProductByMemberAndIds(member, selectedInventoryProductIds))
                .willReturn(0);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> inventoryProductService.updateProfileProducts(1L, profileProductRequest))
                        .isExactlyInstanceOf(SqlUpdateException.class),
                () -> verify(memberRepository).findById(1L),
                () -> verify(inventoryProductRepository).updateBulkProfileProductByMember(member),
                () -> verify(inventoryProductRepository).updateBulkProfileProductByMemberAndIds(member,
                        selectedInventoryProductIds)
        );
    }

    @Test
    void 대표장비_변경_요청에_카테고리가_중복되면_예외를_반환한다() {
        // given
        List<Long> selectedInventoryProductIds = List.of(1L, 2L);
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(selectedInventoryProductIds);
        Member member = MemberFixtures.CORINNE.생성(1L);
        InventoryProduct inventoryProduct1 = SELECTED_INVENTORY_PRODUCT.생성(1L, member, KEYBOARD_1.생성());
        InventoryProduct inventoryProduct2 = UNSELECTED_INVENTORY_PRODUCT.생성(2L, member, KEYBOARD_2.생성());

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));
        given(inventoryProductRepository.findAllById(selectedInventoryProductIds))
                .willReturn(List.of(inventoryProduct1, inventoryProduct2));

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> inventoryProductService.updateProfileProducts(1L, profileProductRequest))
                        .isExactlyInstanceOf(DuplicatedCategoryProfileProductException.class),
                () -> verify(memberRepository).findById(1L),
                () -> verify(inventoryProductRepository).findAllById(selectedInventoryProductIds)
        );
    }

    @Test
    void 대표장비_변경_요청에_소프트웨어_카테고리가_포함되면_예외를_반환한다() {
        // given
        List<Long> selectedInventoryProductIds = List.of(1L, 2L);
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(selectedInventoryProductIds);
        Member member = MemberFixtures.CORINNE.생성(1L);
        InventoryProduct inventoryProduct1 = SELECTED_INVENTORY_PRODUCT.생성(1L, member, SOFTWARE_1.생성());
        InventoryProduct inventoryProduct2 = UNSELECTED_INVENTORY_PRODUCT.생성(2L, member, KEYBOARD_2.생성());

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));
        given(inventoryProductRepository.findAllById(selectedInventoryProductIds))
                .willReturn(List.of(inventoryProduct1, inventoryProduct2));

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> inventoryProductService.updateProfileProducts(1L, profileProductRequest))
                        .isExactlyInstanceOf(InvalidCategoryProfileProductException.class),
                () -> verify(memberRepository).findById(1L),
                () -> verify(inventoryProductRepository).findAllById(selectedInventoryProductIds)
        );
    }

    @Test
    void 등록된_장비를_멤버_id로_조회한다() {
        // given
        Long memberId = 1L;
        Member member = MemberFixtures.CORINNE.생성(memberId);
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, member, KEYBOARD_1.생성(1L));
        given(memberRepository.existsById(1L))
                .willReturn(true);
        given(inventoryProductRepository.findByMemberId(memberId))
                .willReturn(List.of(inventoryProduct));

        // when
        InventoryProductsResponse inventoryProductsResponse = inventoryProductService.findByMemberId(memberId);

        // then
        assertAll(
                () -> assertThat(inventoryProductsResponse.getItems()).hasSize(1)
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(InventoryProductResponse.from(inventoryProduct)),
                () -> verify(memberRepository).existsById(memberId),
                () -> verify(inventoryProductRepository).findByMemberId(memberId)
        );
    }
}
