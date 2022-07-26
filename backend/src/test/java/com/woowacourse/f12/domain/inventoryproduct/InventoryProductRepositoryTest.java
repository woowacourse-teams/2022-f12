package com.woowacourse.f12.domain.inventoryproduct;

import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_2;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.f12.config.JpaConfig;
import com.woowacourse.f12.domain.product.Keyboard;
import com.woowacourse.f12.domain.product.ProductRepository;
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
    private ProductRepository productRepository;

    @Test
    void 멤버_아이디로_인벤토리_상품_목록을_조회한다() {
        // given
        Long myMemberId = 1L;
        Long otherMemberId = 2L;
        Keyboard keyboard1 = 키보드를_저장한다(KEYBOARD_1.생성(1L));
        Keyboard keyboard2 = 키보드를_저장한다(KEYBOARD_2.생성(2L));
        InventoryProduct inventoryProduct1 = SELECTED_INVENTORY_PRODUCT.생성(myMemberId, keyboard1);
        InventoryProduct inventoryProduct2 = SELECTED_INVENTORY_PRODUCT.생성(otherMemberId, keyboard2);
        inventoryProductRepository.saveAll(List.of(inventoryProduct1, inventoryProduct2));

        // when
        List<InventoryProduct> inventoryProducts = inventoryProductRepository.findByMemberId(myMemberId);

        // then
        assertThat(inventoryProducts).containsOnly(inventoryProduct1);
    }

    @Test
    void 멤버_아이디와_상품으로_인벤토리_상품_목록을_조회한다() {
        // given
        Long memberId = 1L;
        Keyboard keyboard = 키보드를_저장한다(KEYBOARD_1.생성(1L));
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(memberId, keyboard);
        inventoryProductRepository.save(inventoryProduct);

        // when
        boolean actual = inventoryProductRepository.existsByMemberIdAndKeyboard(memberId, keyboard);

        // then
        assertThat(actual).isTrue();
    }

    private Keyboard 키보드를_저장한다(Keyboard keyboard) {
        return productRepository.save(keyboard);
    }
}
