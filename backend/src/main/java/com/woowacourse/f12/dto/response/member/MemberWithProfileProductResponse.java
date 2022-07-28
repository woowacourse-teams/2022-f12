package com.woowacourse.f12.dto.response.member;

import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.CareerLevelConstant;
import com.woowacourse.f12.dto.JobTypeConstant;
import com.woowacourse.f12.dto.response.product.KeyboardResponse;
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
    private List<KeyboardResponse> profileProducts;

    private MemberWithProfileProductResponse() {
    }

    public MemberWithProfileProductResponse(final Long id, final String gitHubId, final String name,
                                            final String imageUrl,
                                            final CareerLevelConstant careerLevel, final JobTypeConstant jobType,
                                            final List<KeyboardResponse> profileProducts) {
        this.id = id;
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
        this.profileProducts = profileProducts;
    }

    public static MemberWithProfileProductResponse from(final Member member) {
        final List<KeyboardResponse> profileProducts = member.getInventoryProducts()
                .stream()
                .map(inventoryProduct -> KeyboardResponse.from(inventoryProduct.getKeyboard()))
                .collect(Collectors.toList());
        return new MemberWithProfileProductResponse(member.getId(), member.getGitHubId(), member.getName(),
                member.getImageUrl(), CareerLevelConstant.findByCareerLevel(member.getCareerLevel()),
                JobTypeConstant.findByJobType(member.getJobType()), profileProducts);
    }
}
