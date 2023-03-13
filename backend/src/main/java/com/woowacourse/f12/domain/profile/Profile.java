package com.woowacourse.f12.domain.profile;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProducts;
import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.member.Member;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

@Getter
public class Profile {

    private final Member member;
    private final InventoryProducts inventoryProducts;
    private final boolean isFollowing;

    public Profile(final Member member, final InventoryProducts inventoryProducts, final boolean isFollowing) {
        this.member = member;
        this.inventoryProducts = inventoryProducts;
        this.isFollowing = isFollowing;
    }

    public Profile(final Member member, final boolean isFollowing) {
        this(member, new InventoryProducts(Collections.emptyList()), isFollowing);
    }

    public List<InventoryProduct> getProfileProducts() {
        return inventoryProducts.getProfileProducts();
    }

    public Long getMemberId() {
        return member.getId();
    }

    public String getGithubId() {
        return member.getGitHubId();
    }

    public String getImageUrl() {
        return member.getImageUrl();
    }

    public JobType getJobType() {
        return member.getJobType();
    }

    public CareerLevel getCareerLevel() {
        return member.getCareerLevel();
    }

    public int getFollowerCount() {
        return member.getFollowerCount();
    }

    public String getName() {
        return member.getName();
    }
}
