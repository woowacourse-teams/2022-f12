package com.woowacourse.f12.support.fixture;

import static com.woowacourse.f12.domain.member.CareerLevel.JUNIOR;
import static com.woowacourse.f12.domain.member.CareerLevel.SENIOR;
import static com.woowacourse.f12.domain.member.JobType.BACKEND;
import static com.woowacourse.f12.domain.member.JobType.FRONTEND;
import static com.woowacourse.f12.domain.member.Role.ADMIN;
import static com.woowacourse.f12.domain.member.Role.USER;

import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.Member.MemberBuilder;
import com.woowacourse.f12.domain.member.Role;
import com.woowacourse.f12.dto.response.auth.GitHubProfileResponse;

public enum MemberFixture {

    CORINNE("hamcheeseburger", "유현지", "corinne_url", true, SENIOR, BACKEND, 0, USER),
    MINCHO("jswith", "홍영민", "mincho_url", true, JUNIOR, FRONTEND, 0, USER),
    OHZZI("Ohzzi", "오지훈", "Ohzzi_url", true, JUNIOR, BACKEND, 0, USER),
    CORINNE_UPDATED("hamcheeseburger", "괴물개발자", "corinne_url", true, SENIOR, BACKEND, 0, USER),
    NOT_ADDITIONAL_INFO("invalid", "invalid", "invalid", false, null, null, 0, USER),
    ADMIN_KLAY("yangdongjue5510", "양동주", "klay.url", true, JUNIOR, BACKEND, 0, ADMIN);

    private final String gitHubId;
    private final String name;
    private final String imageUrl;
    private final boolean registered;
    private final CareerLevel careerLevel;
    private final JobType jobType;
    private final int followerCount;
    private final Role role;

    MemberFixture(final String gitHubId, final String name, final String imageUrl, final boolean registered,
                  final CareerLevel careerLevel,
                  final JobType jobType, final int followerCount, final Role role) {
        this.gitHubId = gitHubId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.registered = registered;
        this.careerLevel = careerLevel;
        this.jobType = jobType;
        this.followerCount = followerCount;
        this.role = role;
    }

    public Member 생성() {
        return 생성(null);
    }

    public Member 생성(final Long id) {
        return 기본_정보_빌더_생성(id)
                .build();
    }

    private MemberBuilder 기본_정보_빌더_생성(final Long id) {
        return Member.builder()
                .id(id)
                .gitHubId(this.gitHubId)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .registered(this.registered)
                .careerLevel(this.careerLevel)
                .jobType(this.jobType)
                .followerCount(this.followerCount)
                .role(this.role);
    }

    public GitHubProfileResponse 깃허브_프로필() {
        return new GitHubProfileResponse(this.gitHubId, this.name, this.imageUrl);
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
        return 기본_정보_빌더_생성(id)
                .careerLevel(careerLevel)
                .jobType(jobType)
                .followerCount(followerCount)
                .build();
    }

    public Member 팔로워_카운트를_입력하여_생성(final int followerCount) {
        return 기본_정보_빌더_생성(null)
                .followerCount(followerCount)
                .build();
    }
}
