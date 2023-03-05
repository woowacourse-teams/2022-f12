package com.woowacourse.f12.domain.profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProducts;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.support.fixture.InventoryProductFixtures;
import com.woowacourse.f12.support.fixture.MemberFixture;
import com.woowacourse.f12.support.fixture.ProductFixture;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProfileTest {


    @Test
    void 프로필을_생성해서_정보를_조회한다() {
        // given
        Member member = MemberFixture.CORINNE.생성(1L);
        final Product product = ProductFixture.KEYBOARD_1.생성(1L);
        final InventoryProduct inventoryProduct =
                InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT.생성(1L, member, product);

        // when
        final Profile profile = new Profile(member, new InventoryProducts(List.of(inventoryProduct)), true);

        // then
        assertAll(
                () -> assertThat(profile.isFollowing()).isTrue(),
                () -> assertThat(profile.getProfileProducts()).containsOnly(inventoryProduct),
                () -> assertThat(profile.getImageUrl()).isEqualTo(member.getImageUrl()),
                () -> assertThat(profile.getName()).isEqualTo(member.getName()),
                () -> assertThat(profile.getFollowerCount()).isEqualTo(member.getFollowerCount()),
                () -> assertThat(profile.getGithubId()).isEqualTo(member.getGitHubId()),
                () -> assertThat(profile.getCareerLevel()).isEqualTo(member.getCareerLevel()),
                () -> assertThat(profile.getJobType()).isEqualTo(member.getJobType())
        );
    }
}
