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

    CORINNE("hamcheeseburger", "유현지", "corinne_url", true, SENIOR, BACKEND),
    MINCHO("jswith", "홍영민", "mincho_url", true, JUNIOR, FRONTEND),
    OHZZI("Ohzzi", "오지훈", "Ohzzi_url", true, JUNIOR, BACKEND),
    CORINNE_UPDATED("hamcheeseburger", "괴물개발자", "corinne_url", true, SENIOR, BACKEND),
    NOT_ADDITIONAL_INFO("invalid", "invalid", "invalid", false, null, null);

    private final String gitHubId;
    private final String name;
    private final String imageUrl;
    private final boolean registered;
    private final CareerLevel careerLevel;
    private final JobType jobType;

    MemberFixture(final String gitHubId, final String name, final String imageUrl, final boolean registered, final CareerLevel careerLevel,
                  final JobType jobType) {
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.registered = registered;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
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
                .registered(this.registered)
                .careerLevel(this.careerLevel)
                .jobType(this.jobType)
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
                .registered(this.registered)
                .careerLevel(this.careerLevel)
                .jobType(this.jobType)
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
                .build();
    }

    public Member 추가정보를_입력하여_생성(final CareerLevel careerLevel, final JobType jobType) {
        return 추가정보를_입력하여_생성(null, careerLevel, jobType);
    }

    public Member 추가정보를_입력하여_생성(final Long id, final CareerLevel careerLevel, final JobType jobType) {
        return Member.builder()
                .id(id)
                .gitHubId(this.gitHubId)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .registered(this.registered)
                .careerLevel(careerLevel)
                .jobType(jobType)
                .build();
    }
}
