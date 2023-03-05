package com.woowacourse.f12.dto.response.profile;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.profile.Profile;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.presentation.member.CareerLevelConstant;
import com.woowacourse.f12.presentation.member.JobTypeConstant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class ProfileResponse {

    private Long id;
    private String gitHubId;
    private String name;
    private String imageUrl;
    private CareerLevelConstant careerLevel;
    private JobTypeConstant jobType;
    private List<ProductResponse> profileProducts;
    private int followerCount;
    private boolean following;

    private ProfileResponse() {
    }

    private ProfileResponse(final Long id, final String gitHubId, final String name, final String imageUrl,
                            final CareerLevelConstant careerLevel,
                            final JobTypeConstant jobType, final List<ProductResponse> profileProducts,
                            final int followerCount,
                            final boolean following) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
        this.profileProducts = profileProducts;
        this.followerCount = followerCount;
        this.following = following;
    }

    public static ProfileResponse from(final Profile profile) {
        return new ProfileResponse(
                profile.getMemberId(),
                profile.getGithubId(),
                profile.getName(),
                profile.getImageUrl(),
                CareerLevelConstant.from(profile.getCareerLevel()),
                JobTypeConstant.from(profile.getJobType()),
                convertToProfileProductsResponse(profile.getProfileProducts()),
                profile.getFollowerCount(),
                profile.isFollowing()
        );
    }

    private static List<ProductResponse> convertToProfileProductsResponse(
            final List<InventoryProduct> profileProducts) {
        return profileProducts.stream()
                .map(it -> ProductResponse.from(it.getProduct()))
                .collect(Collectors.toList());
    }
}
