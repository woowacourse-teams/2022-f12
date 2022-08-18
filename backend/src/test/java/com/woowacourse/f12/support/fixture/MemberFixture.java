package com.woowacourse.f12.support.fixture;

import static com.woowacourse.f12.acceptance.support.RestAssuredRequestUtil.GET_요청을_보낸다;
import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.FRONTEND;
import static com.woowacourse.f12.support.fixture.GitHubProfileFixtures.CORINNE_GITHUB;
import static com.woowacourse.f12.support.fixture.GitHubProfileFixtures.MINCHO_GITHUB;
import static com.woowacourse.f12.support.fixture.GitHubProfileFixtures.OHZZI_GITHUB;

import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProducts;
import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.dto.response.auth.GitHubProfileResponse;
import com.woowacourse.f12.dto.response.auth.LoginResponse;
import java.util.List;

public enum MemberFixture {

    CORINNE(CORINNE_GITHUB.getCode(), "hamcheeseburger", "유현지", "corinne_url", SENIOR, BACKEND),
    MINCHO(MINCHO_GITHUB.getCode(), "jswith", "홍영민", "mincho_url", JUNIOR, FRONTEND),
    OHZZI(OHZZI_GITHUB.getCode(), "Ohzzi", "오지훈", "Ohzzi_url", JUNIOR, BACKEND),
    CORINNE_UPDATED(CORINNE_GITHUB.getCode(), "hamcheeseburger", "괴물개발자", "corinne_url", SENIOR, BACKEND),
    NOT_ADDITIONAL_INFO(null, "invalid", "invalid", "invalid", null, null);

    private final String gitHubLoginCode;
    private final String gitHubId;
    private final String name;
    private final String imageUrl;
    private final CareerLevel careerLevel;
    private final JobType jobType;

    MemberFixture(final String gitHubLoginCode, final String gitHubId, final String name, final String imageUrl,
                  final CareerLevel careerLevel,
                  final JobType jobType) {
        this.gitHubLoginCode = gitHubLoginCode;
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
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
                .careerLevel(this.careerLevel)
                .jobType(this.jobType)
                .build();
    }

    public GitHubProfileResponse 깃허브_프로필() {
        return new GitHubProfileResponse(this.gitHubId, this.name, this.imageUrl);
    }

    public Member 인벤토리를_추가해서_생성(final Long id, final InventoryProduct... inventoryProducts) {
        return Member.builder()
                .id(id)
                .gitHubId(this.gitHubId)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .careerLevel(this.careerLevel)
                .jobType(this.jobType)
                .inventoryProducts(new InventoryProducts(List.of(inventoryProducts)))
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
                .careerLevel(careerLevel)
                .jobType(jobType)
                .build();
    }

    public LoginResponse 로그인을_한다() {
        return GET_요청을_보낸다("/api/v1/login?code=" + gitHubLoginCode)
                .as(LoginResponse.class);
    }

    public AuthorizedAction 로그인을_하고() {
        String token = GET_요청을_보낸다("/api/v1/login?code=" + gitHubLoginCode)
                .as(LoginResponse.class)
                .getToken();
        return new AuthorizedAction(token);
    }

    public AuthorizedAction 로그인한_상태로(final String token) {
        return new AuthorizedAction(token);
    }
}
