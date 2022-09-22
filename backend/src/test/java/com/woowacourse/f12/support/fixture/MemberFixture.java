package com.woowacourse.f12.support.fixture;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProducts;
import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.response.auth.GitHubProfileResponse;

import java.util.List;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.FRONTEND;

public enum MemberFixture {

    CORINNE("hamcheeseburger", "유현지", "corinne_url", SENIOR, BACKEND, 0),
    MINCHO("jswith", "홍영민", "mincho_url", JUNIOR, FRONTEND, 0),
    OHZZI("Ohzzi", "오지훈", "Ohzzi_url", JUNIOR, BACKEND, 0),
    CORINNE_UPDATED("hamcheeseburger", "괴물개발자", "corinne_url", SENIOR, BACKEND, 0),
    NOT_ADDITIONAL_INFO("invalid", "invalid", "invalid", null, null, 0);

    private final String gitHubId;
    private final String name;
    private final String imageUrl;
    private final CareerLevel careerLevel;
    private final JobType jobType;
    private final int followerCount;

    MemberFixture(final String gitHubId, final String name, final String imageUrl, final CareerLevel careerLevel,
                  final JobType jobType, final int followerCount) {
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
        this.followerCount = followerCount;
    }

    public Member 생성() {
        return 생성(null);
    }

    public Member 생성(final Long id) {
        return Member.builder()
                .id(id)
                .gitHubId(this.gitHubId)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .careerLevel(this.careerLevel)
                .jobType(this.jobType)
                .followerCount(this.followerCount)
                .build();
    }

    public GitHubProfileResponse 깃허브_프로필() {
        return new GitHubProfileResponse(this.gitHubId, this.name, this.imageUrl);
    }

    public Member 인벤토리를_추가해서_생성(final Long id, final List<InventoryProduct> inventoryProducts) {
        return Member.builder()
                .id(id)
                .gitHubId(this.gitHubId)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .careerLevel(this.careerLevel)
                .jobType(this.jobType)
                .followerCount(this.followerCount)
                .inventoryProducts(new InventoryProducts(inventoryProducts))
                .build();
    }

    public Member 추가정보와_인벤토리를_추가해서_생성(final Long id, final CareerLevel careerLevel, final JobType jobType, final List<InventoryProduct> inventoryProducts) {
        return Member.builder()
                .id(id)
                .gitHubId(this.gitHubId)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .careerLevel(careerLevel)
                .jobType(jobType)
                .inventoryProducts(new InventoryProducts(inventoryProducts))
                .build();
    }

    public Member 추가정보_없이_생성(final Long id) {
        return Member.builder()
                .id(id)
                .gitHubId(this.gitHubId)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .followerCount(this.followerCount)
                .build();
    }

    public Member 추가정보를_입력하여_생성(final CareerLevel careerLevel, final JobType jobType) {
        return 추가정보를_입력하여_생성(null, careerLevel, jobType);
    }

    public Member 추가정보를_입력하여_생성(final Long id, final CareerLevel careerLevel, final JobType jobType) {
        return 추가정보와_팔로워_카운트를_입력하여_생성(id, careerLevel, jobType, this.followerCount);
    }

    public Member 추가정보와_팔로워_카운트를_입력하여_생성(final Long id, final CareerLevel careerLevel, final JobType jobType, final int followerCount) {
        return Member.builder()
                .id(id)
                .gitHubId(this.gitHubId)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .careerLevel(careerLevel)
                .jobType(jobType)
                .followerCount(followerCount)
                .build();
    }

    public Member 팔로워_카운트를_입력하여_생성(final int followerCount) {
        return Member.builder()
                .gitHubId(this.gitHubId)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .careerLevel(this.careerLevel)
                .jobType(this.jobType)
                .followerCount(followerCount)
                .build();
    }
}
