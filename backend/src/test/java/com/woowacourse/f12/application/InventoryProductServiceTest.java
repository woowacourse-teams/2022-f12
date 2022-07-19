package com.woowacourse.f12.application;

import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_2;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import com.woowacourse.f12.domain.InventoryProduct;
import com.woowacourse.f12.domain.InventoryProductRepository;
import com.woowacourse.f12.domain.MemberRepository;
import com.woowacourse.f12.dto.request.ProfileProductRequest;
import com.woowacourse.f12.dto.response.InventoryProductResponse;
import com.woowacourse.f12.dto.response.InventoryProductsResponse;
import com.woowacourse.f12.exception.InvalidProfileProductException;
import com.woowacourse.f12.support.KeyboardFixtures;
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
    void 대표_장비를_등록한다() {
        // given
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(1L, 2L);
        InventoryProduct selectInventoryProduct = InventoryProduct.builder()
                .id(1L)
                .memberId(1L)
                .keyboard(KEYBOARD_1.생성())
                .isSelected(false)
                .build();
        InventoryProduct unselectInventoryProduct = InventoryProduct.builder()
                .id(2L)
                .memberId(1L)
                .keyboard(KEYBOARD_2.생성())
                .isSelected(true)
                .build();
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(MemberFixtures.CORINNE.생성(1L)));
        given(inventoryProductRepository.findById(1L))
                .willReturn(Optional.of(selectInventoryProduct));
        given(inventoryProductRepository.findById(2L))
                .willReturn(Optional.of(unselectInventoryProduct));

        // when
        inventoryProductService.updateProfileProducts(1L, profileProductRequest);

        // then
        assertAll(
                () -> verify(memberRepository).findById(1L),
                () -> verify(inventoryProductRepository).findById(1L),
                () -> verify(inventoryProductRepository).findById(2L)
        );
    }

    @Test
    void 대표_장비를_업데이트할_때_요청된_장비가_모두_null인_경우_예외가_발생한다() {
        // given
        ProfileProductRequest profileProductRequest = new ProfileProductRequest(null, null);
        given(memberRepository.findById(1L))
                .willReturn(Optional.of(MemberFixtures.CORINNE.생성(1L)));

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> inventoryProductService.updateProfileProducts(1L, profileProductRequest))
                        .isExactlyInstanceOf(InvalidProfileProductException.class),
                () -> verify(memberRepository).findById(1L)
        );
    }

    @Test
    void 등록된_장비를_멤버_id로_조회한다() {
        // given
        Long memberId = 1L;
        InventoryProduct inventoryProduct = InventoryProduct.builder()
                .id(1L)
                .memberId(memberId)
                .keyboard(KeyboardFixtures.KEYBOARD_1.생성(1L))
                .isSelected(true)
                .build();
        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(CORINNE.생성(1L)));
        given(inventoryProductRepository.findByMemberId(memberId))
                .willReturn(List.of(inventoryProduct));

        // when
        InventoryProductsResponse inventoryProductsResponse = inventoryProductService.findByMemberId(memberId);

        // then
        assertAll(
                () -> assertThat(inventoryProductsResponse.getKeyboards()).hasSize(1)
                        .usingRecursiveFieldByFieldElementComparator()
                        .containsOnly(InventoryProductResponse.from(inventoryProduct)),
                () -> verify(memberRepository).findById(memberId),
                () -> verify(inventoryProductRepository).findByMemberId(memberId)
        );
    }
}
