package com.woowacourse.f12.dto.response.member;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.response.product.ProductResponse;
import com.woowacourse.f12.presentation.member.CareerLevelConstant;
import com.woowacourse.f12.presentation.member.JobTypeConstant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class MemberWithProfileProductResponse {

    private Long id;
    private String gitHubId;
    private String name;
    private String imageUrl;
    private CareerLevelConstant careerLevel;
    private JobTypeConstant jobType;
    private List<ProductResponse> profileProducts;

    private MemberWithProfileProductResponse() {
    }

    public MemberWithProfileProductResponse(final Long id, final String gitHubId, final String name,
                                            final String imageUrl,
                                            final CareerLevelConstant careerLevel, final JobTypeConstant jobType,
                                            final List<ProductResponse> profileProducts) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
        this.profileProducts = profileProducts;
    }

    public static MemberWithProfileProductResponse from(final Member member) {
        final List<ProductResponse> profileProducts = member.getProfileProducts()
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return new MemberWithProfileProductResponse(member.getId(), member.getGitHubId(), member.getName(),
                member.getImageUrl(), CareerLevelConstant.from(member.getCareerLevel()),
                JobTypeConstant.from(member.getJobType()), profileProducts);
    }
}
