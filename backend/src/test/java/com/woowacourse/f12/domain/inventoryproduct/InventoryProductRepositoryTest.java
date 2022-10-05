package com.woowacourse.f12.domain.inventoryproduct;

import static com.woowacourse.f12.support.fixture.InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.fixture.InventoryProductFixtures.UNSELECTED_INVENTORY_PRODUCT;
import static com.woowacourse.f12.support.fixture.MemberFixture.CORINNE;
import static com.woowacourse.f12.support.fixture.MemberFixture.MINCHO;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_1;
import static com.woowacourse.f12.support.fixture.ProductFixture.KEYBOARD_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.f12.domain.RepositoryTest;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.domain.product.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@RepositoryTest
class InventoryProductRepositoryTest {

    @Autowired
    private InventoryProductRepository inventoryProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 멤버_아이디로_인벤토리_상품_목록을_조회한다() {
        // given
        Member me = 회원을_저장한다(CORINNE.생성());
        Member otherMember = 회원을_저장한다(MINCHO.생성());
        Product product1 = 제품을_저장한다(KEYBOARD_1.생성());
        Product product2 = 제품을_저장한다(KEYBOARD_2.생성());
        InventoryProduct inventoryProduct1 = SELECTED_INVENTORY_PRODUCT.생성(me, product1);
        InventoryProduct inventoryProduct2 = SELECTED_INVENTORY_PRODUCT.생성(otherMember, product2);
        inventoryProductRepository.saveAll(List.of(inventoryProduct1, inventoryProduct2));

        // when
        List<InventoryProduct> inventoryProducts = inventoryProductRepository.findWithProductByMemberId(me.getId());

        // then
        assertThat(inventoryProducts).containsOnly(inventoryProduct1);
    }

    @Test
    void 멤버_아이디와_상품으로_인벤토리_상품_목록을_조회한다() {
        // given다
        Member member = 회원을_저장한다(CORINNE.생성());
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        장비를_등록한다(SELECTED_INVENTORY_PRODUCT.생성(member, product));

        // when
        boolean actual = inventoryProductRepository.existsByMemberAndProduct(member, product);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 인벤토리_상품_모두를_대표_장비에서_해제한다() {
        // given
        Member member = 회원을_저장한다(CORINNE.생성());
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        장비를_등록한다(UNSELECTED_INVENTORY_PRODUCT.생성(member, product));

        // when
        int actual = inventoryProductRepository.updateBulkProfileProductByMember(member, false);

        // then
        assertThat(actual).isOne();
    }

    @Test
    void 인벤토리_상품을_대표_장비로_등록한다() {
        // given
        Member member = 회원을_저장한다(CORINNE.생성());
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        InventoryProduct inventoryProduct = 장비를_등록한다(UNSELECTED_INVENTORY_PRODUCT.생성(member, product));

        // when
        int actual = inventoryProductRepository.updateBulkProfileProductByMemberAndIds(member,
                List.of(inventoryProduct.getId()), true);

        // then
        assertThat(actual).isOne();
    }

    @Test
    void 멤버와_제품으로_인벤토리_제품을_조회한다() {
        // given
        Member member = 회원을_저장한다(CORINNE.생성());
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        장비를_등록한다(UNSELECTED_INVENTORY_PRODUCT.생성(member, product));

        // when
        Optional<InventoryProduct> actual = inventoryProductRepository.findWithProductByMemberAndProduct(member,
                product);

        // then
        assertThat(actual).isPresent();
    }

    @Test
    void 멤버_목록으로_제품이_포함된_인벤토리를_조회한다() {
        // given
        Member corinne = 회원을_저장한다(CORINNE.생성());
        Member mincho = 회원을_저장한다(MINCHO.생성());
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        InventoryProduct inventoryProduct = 장비를_등록한다(UNSELECTED_INVENTORY_PRODUCT.생성(corinne, product));

        // when
        List<InventoryProduct> actual = inventoryProductRepository.findWithProductByMembers(List.of(corinne, mincho));

        // then
        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .hasSize(1)
                .containsExactly(inventoryProduct);
    }

    @Test
    void 동일_제품으로_보유_장비를_중복해서_등록할_수_없다() {
        // given
        Member mincho = 회원을_저장한다(MINCHO.생성());
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        InventoryProduct inventoryProduct1 = UNSELECTED_INVENTORY_PRODUCT.생성(mincho, product);
        InventoryProduct inventoryProduct2 = UNSELECTED_INVENTORY_PRODUCT.생성(mincho, product);
        inventoryProductRepository.save(inventoryProduct1);

        // when, then
        assertThatThrownBy(() -> inventoryProductRepository.save(inventoryProduct2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 특정_제품에_대한_인벤토리를_삭제한다() {
        // given
        Member mincho = 회원을_저장한다(MINCHO.생성());
        Member corinne = 회원을_저장한다(CORINNE.생성());
        Product product = 제품을_저장한다(KEYBOARD_1.생성());
        InventoryProduct inventoryProduct1 = UNSELECTED_INVENTORY_PRODUCT.생성(mincho, product);
        InventoryProduct inventoryProduct2 = UNSELECTED_INVENTORY_PRODUCT.생성(corinne, product);
        inventoryProductRepository.save(inventoryProduct1);
        inventoryProductRepository.save(inventoryProduct2);

        // when
        inventoryProductRepository.deleteByProduct(product);

        // then
        final long count = inventoryProductRepository.count();
        assertThat(count).isZero();
    }

    private Product 제품을_저장한다(Product product) {
        return productRepository.save(product);
    }

    private Member 회원을_저장한다(Member member) {
        return memberRepository.save(member);
    }

    private InventoryProduct 장비를_등록한다(InventoryProduct inventoryProduct) {
        return inventoryProductRepository.save(inventoryProduct);
    }
}
