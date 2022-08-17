package com.woowacourse.f12.application.member;


import com.woowacourse.f12.domain.member.*;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.request.member.MemberSearchRequest;
import com.woowacourse.f12.dto.response.member.LoggedInMemberResponse;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.exception.badrequest.AlreadyFollowingException;
import com.woowacourse.f12.exception.badrequest.NotFollowingException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import com.woowacourse.f12.presentation.member.CareerLevelConstant;
import com.woowacourse.f12.presentation.member.JobTypeConstant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final FollowingRepository followingRepository;

    public MemberService(MemberRepository memberRepository, FollowingRepository followingRepository) {
        this.memberRepository = memberRepository;
        this.followingRepository = followingRepository;
    }

    public LoggedInMemberResponse findByLoggedInId(final Long loggedInId) {
        final Member member = findMember(loggedInId);
        return LoggedInMemberResponse.from(member);
    }

    public MemberResponse findById(final Long targetId, final Long loggedInId) {
        final Member member = findMember(targetId);
        final boolean following = isFollowing(loggedInId, targetId);
        return MemberResponse.from(member, following);
    }

    private boolean isFollowing(@Nullable final Long followerId, final Long followeeId) {
        return Objects.nonNull(followerId) && followingRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    @Transactional
    public void updateMember(final Long memberId, final MemberRequest memberRequest) {
        final Member member = findMember(memberId);
        member.update(memberRequest.toMember());
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    public MemberPageResponse findByContains(final MemberSearchRequest memberSearchRequest, final Pageable pageable) {
        final CareerLevel careerLevel = parseCareerLevel(memberSearchRequest);
        final JobType jobType = parseJobType(memberSearchRequest);
        final Slice<Member> slice = memberRepository.findBySearchConditions(memberSearchRequest.getQuery(), careerLevel,
                jobType, pageable);
        return MemberPageResponse.from(slice);
    }

    private JobType parseJobType(final MemberSearchRequest memberSearchRequest) {
        final JobTypeConstant jobTypeConstant = memberSearchRequest.getJobType();
        if (Objects.isNull(jobTypeConstant)) {
            return null;
        }
        return jobTypeConstant.toJobType();
    }

    private CareerLevel parseCareerLevel(final MemberSearchRequest memberSearchRequest) {
        final CareerLevelConstant careerLevelConstant = memberSearchRequest.getCareerLevel();
        if (Objects.isNull(careerLevelConstant)) {
            return null;
        }
        return careerLevelConstant.toCareerLevel();
    }

    public void follow(final Long followerId, final Long followeeId) {
        validateFollowingMembersExist(followerId, followeeId);
        validateNotFollowing(followerId, followeeId);
        final Following following = Following.builder()
                .followerId(followerId)
                .followeeId(followeeId)
                .build();
        followingRepository.save(following);
    }

    private void validateFollowingMembersExist(final Long followerId, final Long followeeId) {
        if (!memberRepository.existsById(followerId) || !memberRepository.existsById(followeeId)) {
            throw new MemberNotFoundException();
        }
    }

    public void unfollow(final Long followerId, final Long followeeId) {
        validateFollowingMembersExist(followerId, followeeId);
        final Following following = followingRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(NotFollowingException::new);
        followingRepository.delete(following);
    }

    private void validateNotFollowing(final Long followerId, final Long followeeId) {
        if (followingRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            throw new AlreadyFollowingException();
        }
    }
}
