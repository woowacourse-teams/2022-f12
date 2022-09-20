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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private static final boolean NOT_LOGGED_IN_FOLLOWING_STATE = false;

    private final MemberRepository memberRepository;
    private final FollowingRepository followingRepository;

    public MemberService(final MemberRepository memberRepository, final FollowingRepository followingRepository) {
        this.memberRepository = memberRepository;
        this.followingRepository = followingRepository;
    }

    public LoggedInMemberResponse findLoggedInMember(final Long loggedInId) {
        final Member member = findMember(loggedInId);
        return LoggedInMemberResponse.from(member);
    }

    public MemberResponse find(final Long targetId, @Nullable final Long loggedInId) {
        final Member member = findMember(targetId);
        if (isNotLoggedIn(loggedInId)) {
            return MemberResponse.of(member, NOT_LOGGED_IN_FOLLOWING_STATE);
        }
        final boolean following = isFollowing(loggedInId, targetId);
        return MemberResponse.of(member, following);
    }

    private boolean isNotLoggedIn(final Long loggedInId) {
        return Objects.isNull(loggedInId);
    }

    private boolean isFollowing(final Long followerId, final Long followingId) {
        return followingRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
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

    public MemberPageResponse findByContains(@Nullable final Long loggedInId, final MemberSearchRequest memberSearchRequest, final Pageable pageable) {
        final CareerLevel careerLevel = parseCareerLevel(memberSearchRequest);
        final JobType jobType = parseJobType(memberSearchRequest);
        final Slice<Member> slice = memberRepository.findBySearchConditions(memberSearchRequest.getQuery(), careerLevel,
                jobType, pageable);
        if (isNotLoggedIn(loggedInId)) {
            return MemberPageResponse.ofByFollowingCondition(slice, false);
        }
        final List<Following> followings = followingRepository.findByFollowerIdAndFollowingIdIn(loggedInId, extractMemberIds(slice.getContent()));
        return MemberPageResponse.of(slice, followings);
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

    private List<Long> extractMemberIds(final List<Member> members) {
        return members.stream()
                .map(Member::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void follow(final Long followerId, final Long followingId) {
        final Member followingMember = memberRepository.findById(followingId)
                .orElseThrow(MemberNotFoundException::new);
        validateFollowingMembersExist(followerId);
        validateNotFollowing(followerId, followingId);
        final Following following = Following.builder()
                .followerId(followerId)
                .followingId(followingId)
                .build();
        followingRepository.save(following);
        increaseFollowerCount(followingMember);
    }

    private void validateFollowingMembersExist(final Long followerId) {
        if (!memberRepository.existsById(followerId)) {
            throw new MemberNotFoundException();
        }
    }

    private void validateNotFollowing(final Long followerId, final Long followingId) {
        if (followingRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new AlreadyFollowingException();
        }
    }

    private void increaseFollowerCount(final Member followingMember) {
        final Member updatedMember = Member.builder()
                .followerCount(followingMember.getFollowerCount() + 1)
                .build();
        followingMember.update(updatedMember);
    }

    @Transactional
    public void unfollow(final Long followerId, final Long followingId) {
        final Member followingMember = memberRepository.findById(followingId)
                .orElseThrow(MemberNotFoundException::new);
        validateFollowingMembersExist(followerId);
        final Following following = findFollowingRelation(followerId, followingId);
        followingRepository.delete(following);
        decreaseFollowerCount(followingMember);
    }

    private Following findFollowingRelation(final Long followerId, final Long followingId) {
        return followingRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(NotFollowingException::new);
    }

    private void decreaseFollowerCount(final Member followingMember) {
        final Member updatedMember = Member.builder()
                .followerCount(followingMember.getFollowerCount() - 1)
                .build();
        followingMember.update(updatedMember);
    }

    public MemberPageResponse findFollowingsByConditions(final Long loggedInId, final MemberSearchRequest memberSearchRequest,
                                                         final Pageable pageable) {
        final CareerLevel careerLevel = parseCareerLevel(memberSearchRequest);
        final JobType jobType = parseJobType(memberSearchRequest);
        final Slice<Member> slice = memberRepository.findFollowingsBySearchConditions(loggedInId, memberSearchRequest.getQuery(),
                careerLevel, jobType, pageable);
        return MemberPageResponse.ofByFollowingCondition(slice, true);
    }
}
