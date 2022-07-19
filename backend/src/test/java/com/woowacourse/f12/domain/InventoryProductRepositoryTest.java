package com.woowacourse.f12.domain;

import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_2;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.f12.config.JpaConfig;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
class InventoryProductRepositoryTest {

    @Autowired
    private InventoryProductRepository inventoryProductRepository;

    @Autowired
    private KeyboardRepository keyboardRepository;

    @Test
    void 멤버_아이디로_인벤토리_상품_목록을_조회한다() {
        // given
        Long myMemberId = 1L;
        Long otherMemberId = 2L;
        Keyboard keyboard1 = 키보드를_저장한다(KEYBOARD_1.생성(1L));
        Keyboard keyboard2 = 키보드를_저장한다(KEYBOARD_2.생성(2L));
        InventoryProduct inventoryProduct1 = InventoryProduct.builder()
                .memberId(myMemberId)
                .keyboard(keyboard1)
                .build();
        InventoryProduct inventoryProduct2 = InventoryProduct.builder()
                .memberId(otherMemberId)
                .keyboard(keyboard2)
                .build();
        inventoryProductRepository.saveAll(List.of(inventoryProduct1, inventoryProduct2));

        // when
        List<InventoryProduct> inventoryProducts = inventoryProductRepository.findByMemberId(myMemberId);

        // then
        assertThat(inventoryProducts).containsOnly(inventoryProduct1);
    }

    private Keyboard 키보드를_저장한다(Keyboard keyboard) {
        return keyboardRepository.save(keyboard);
    }
}
