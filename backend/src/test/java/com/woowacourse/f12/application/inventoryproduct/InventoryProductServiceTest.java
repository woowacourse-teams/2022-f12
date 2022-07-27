package com.woowacourse.f12.application.inventoryproduct;

import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.InventoryProductFixtures.UNSELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.ProductFixture.KEYBOARD_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.request.inventoryproduct.ProfileProductRequest;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductResponse;
import com.woowacourse.f12.dto.response.inventoryproduct.InventoryProductsResponse;
import com.woowacourse.f12.exception.badrequest.InvalidProfileProductException;
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
    void 대표_장비를_등록한다() {
        // given
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(1L, 2L);
        InventoryProduct inventoryProduct1 = UNSELECTED_INVENTORY_PRODUCT.생성(1L, 1L, KEYBOARD_1.생성());
        InventoryProduct inventoryProduct2 = SELECTED_INVENTORY_PRODUCT.생성(2L, 1L, KEYBOARD_2.생성());
        given(memberRepository.existsById(1L))
                .willReturn(true);
        given(inventoryProductRepository.findById(1L))
                .willReturn(Optional.of(inventoryProduct1));
        given(inventoryProductRepository.findById(2L))
                .willReturn(Optional.of(inventoryProduct2));

        // when
        inventoryProductService.updateProfileProducts(1L, profileProductRequest);

        // then
        assertAll(
                () -> verify(memberRepository).existsById(1L),
                () -> verify(inventoryProductRepository).findById(1L),
                () -> verify(inventoryProductRepository).findById(2L)
        );
    }

    @Test
    void 대표_장비를_업데이트할_때_요청된_장비가_모두_null인_경우_예외가_발생한다() {
        // given
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(null, null);
        given(memberRepository.existsById(1L))
                .willReturn(true);

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> inventoryProductService.updateProfileProducts(1L, profileProductRequest))
                        .isExactlyInstanceOf(InvalidProfileProductException.class),
                () -> verify(memberRepository).existsById(1L)
        );
    }

    @Test
    void 등록된_장비를_멤버_id로_조회한다() {
        // given
        Long memberId = 1L;
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(1L, memberId, KEYBOARD_1.생성(1L));
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
