package com.woowacourse.f12.domain.inventoryproduct;

import static com.woowacourse.f12.support.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_1;
import static com.woowacourse.f12.support.KeyboardFixtures.KEYBOARD_2;
import static com.woowacourse.f12.support.MemberFixtures.CORINNE;
import static com.woowacourse.f12.support.MemberFixtures.MINCHO;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.f12.config.JpaConfig;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.domain.product.Keyboard;
import com.woowacourse.f12.domain.product.KeyboardRepository;
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

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 멤버_아이디로_인벤토리_상품_목록을_조회한다() {
        // given
        Member myMember = 회원을_저장한다(CORINNE.생성());
        Member otherMember = 회원을_저장한다(MINCHO.생성());
        Keyboard keyboard1 = 키보드를_저장한다(KEYBOARD_1.생성());
        Keyboard keyboard2 = 키보드를_저장한다(KEYBOARD_2.생성());
        InventoryProduct inventoryProduct1 = SELECTED_INVENTORY_PRODUCT.생성(myMember, keyboard1);
        InventoryProduct inventoryProduct2 = SELECTED_INVENTORY_PRODUCT.생성(otherMember, keyboard2);
        inventoryProductRepository.saveAll(List.of(inventoryProduct1, inventoryProduct2));

        // when
        List<InventoryProduct> inventoryProducts = inventoryProductRepository.findByMemberId(myMember.getId());

        // then
        assertThat(inventoryProducts).containsOnly(inventoryProduct1);
    }

    @Test
    void 멤버_아이디와_상품으로_인벤토리_상품_목록을_조회한다() {
        // given
        Member member = 회원을_저장한다(CORINNE.생성());
        Keyboard keyboard = 키보드를_저장한다(KEYBOARD_1.생성());
        InventoryProduct inventoryProduct = SELECTED_INVENTORY_PRODUCT.생성(member, keyboard);
        inventoryProductRepository.save(inventoryProduct);

        // when
        boolean actual = inventoryProductRepository.existsByMemberAndKeyboard(member, keyboard);

        // then
        assertThat(actual).isTrue();
    }

    private Keyboard 키보드를_저장한다(Keyboard keyboard) {
        return keyboardRepository.save(keyboard);
    }

    private Member 회원을_저장한다(Member member) {
        return memberRepository.save(member);
    }
}
