package com.woowacourse.f12.domain.profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.member.Following;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.product.Product;
import com.woowacourse.f12.support.fixture.InventoryProductFixtures;
import com.woowacourse.f12.support.fixture.MemberFixture;
import com.woowacourse.f12.support.fixture.ProductFixture;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProfilesTest {

    final Product product = ProductFixture.KEYBOARD_1.생성(1L);
    final Member member = MemberFixture.CORINNE.생성(1L);

    @Test
    void 대표장비가_있고_팔로잉하는_프로필들을_생성한다() {
        // given
        final InventoryProduct inventoryProduct =
                InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT.생성(1L, member, product);

        final Following following = Following.builder()
                .id(1L)
                .followerId(2L)
                .followingId(member.getId())
                .createdAt(LocalDateTime.now())
                .build();

        // when
        final Profiles profiles = Profiles.of(List.of(member), List.of(inventoryProduct), List.of(following));

        // then
        assertAll(
                () -> assertThat(profiles.getProfiles()).hasSize(1),
                () -> assertThat(profiles.getProfiles().get(0).isFollowing()).isTrue(),
                () -> assertThat(profiles.getProfiles().get(0).getProfileProducts().size()).isOne()
        );
    }

    @Test
    void 팔로잉하지_않는_프로필을_생성한다() {
        final InventoryProduct inventoryProduct =
                InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT.생성(1L, member, product);

        // when
        final Profiles profiles = Profiles.of(List.of(member), List.of(inventoryProduct), Collections.emptyList());

        // then
        assertAll(
                () -> assertThat(profiles.getProfiles()).hasSize(1),
                () -> assertThat(profiles.getProfiles().get(0).isFollowing()).isFalse(),
                () -> assertThat(profiles.getProfiles().get(0).getProfileProducts().size()).isOne()
        );
    }

    @Test
    void 대표장비가_없는_프로필을_생성한다() {
        // given
        final InventoryProduct inventoryProduct =
                InventoryProductFixtures.UNSELECTED_INVENTORY_PRODUCT.생성(1L, member, product);

        // when
        final Profiles profiles = Profiles.of(List.of(member), List.of(inventoryProduct), Collections.emptyList());

        // then
        assertAll(
                () -> assertThat(profiles.getProfiles()).hasSize(1),
                () -> assertThat(profiles.getProfiles().get(0).getProfileProducts()).isEmpty()
        );
    }

    @Test
    void 팔로잉하는_프로필을_팔로잉_관계를_생략하고_생성한다() {
        // given
        final InventoryProduct inventoryProduct =
                InventoryProductFixtures.SELECTED_INVENTORY_PRODUCT.생성(1L, member, product);

        // when
        final Profiles profiles = Profiles.ofFollowings(List.of(member), List.of(inventoryProduct));

        assertAll(
                () -> assertThat(profiles.getProfiles()).hasSize(1),
                () -> assertThat(profiles.getProfiles().get(0).isFollowing()).isTrue()
        );
    }
}
