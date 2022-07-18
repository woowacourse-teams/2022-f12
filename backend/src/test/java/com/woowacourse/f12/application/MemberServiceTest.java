package com.woowacourse.f12.application;

import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_2;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import com.woowacourse.f12.domain.InventoryProduct;
import com.woowacourse.f12.domain.InventoryProductRepository;
import com.woowacourse.f12.domain.MemberRepository;
import com.woowacourse.f12.dto.request.ProfileProductRequest;
import com.woowacourse.f12.support.MemberFixtures;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private InventoryProductRepository inventoryProductRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

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
        memberService.updateProfileProducts(1L, profileProductRequest);

        // then
        assertAll(
                () -> verify(memberRepository).findById(1L),
                () -> verify(inventoryProductRepository).findById(1L),
                () -> verify(inventoryProductRepository).findById(2L)
        );
    }
}
