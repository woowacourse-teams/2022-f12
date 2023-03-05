package com.woowacourse.f12.domain.profile;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProducts;
import com.woowacourse.f12.domain.member.Following;
import com.woowacourse.f12.domain.member.Member;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class Profiles {

    private final List<Profile> profiles;

    private Profiles(final List<Profile> profiles) {
        this.profiles = profiles;
    }

    public static Profiles of(final List<Member> members, final List<InventoryProduct> mixedInventoryProducts,
                              final List<Following> followingRelations) {
        final Map<Long, List<InventoryProduct>> inventoryProductsGroups = groupByMemberId(mixedInventoryProducts);
        final List<Profile> profiles = members.stream()
                .map(member -> createProfile(member, inventoryProductsGroups, followingRelations))
                .collect(Collectors.toList());
        return new Profiles(profiles);
    }

    private static Profile createProfile(final Member member,
                                         final Map<Long, List<InventoryProduct>> inventoryProductsGroups,
                                         final List<Following> followingRelations) {
        return new Profile(member,
                new InventoryProducts(inventoryProductsGroups.get(member.getId())),
                isFollowing(followingRelations, member.getId()));
    }

    private static Map<Long, List<InventoryProduct>> groupByMemberId(final List<InventoryProduct> inventoryProducts) {
        return inventoryProducts.stream()
                .collect(Collectors.groupingBy(
                        it -> it.getMember().getId(),
                        Collectors.toList()));
    }

    private static boolean isFollowing(final List<Following> followingRelations, final Long memberId) {
        return followingRelations.stream()
                .anyMatch(it -> it.isFollowing(memberId));
    }
}
