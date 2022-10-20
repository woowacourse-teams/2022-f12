package com.woowacourse.f12.application.member;


import com.woowacourse.f12.domain.inventoryproduct.InventoryProduct;
import com.woowacourse.f12.domain.inventoryproduct.InventoryProductRepository;
import com.woowacourse.f12.domain.member.CareerLevel;
import com.woowacourse.f12.domain.member.Following;
import com.woowacourse.f12.domain.member.FollowingRepository;
import com.woowacourse.f12.domain.member.JobType;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
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
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private static final boolean NOT_LOGGED_IN_FOLLOWING_STATE = false;

    private final MemberRepository memberRepository;
    private final FollowingRepository followingRepository;
    private final InventoryProductRepository inventoryProductRepository;

    public MemberService(final MemberRepository memberRepository, final FollowingRepository followingRepository,
                         final InventoryProductRepository inventoryProductRepository) {
        this.memberRepository = memberRepository;
        this.followingRepository = followingRepository;
        this.inventoryProductRepository = inventoryProductRepository;
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
        return loggedInId == null;
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

    public MemberPageResponse findBySearchConditions(@Nullable final Long loggedInId,
                                                     final MemberSearchRequest memberSearchRequest,
                                                     final Pageable pageable) {
        final Slice<Member> slice = findBySearchConditions(memberSearchRequest, pageable);
        if (slice.isEmpty()) {
            return MemberPageResponse.ofByFollowingCondition(slice, false);
        }
        setInventoryProductsToMembers(slice);
        if (isNotLoggedIn(loggedInId)) {
            return MemberPageResponse.ofByFollowingCondition(slice, false);
        }
        final List<Following> followings = followingRepository.findByFollowerIdAndFollowingIdIn(loggedInId,
                extractMemberIds(slice.getContent()));
        return MemberPageResponse.of(slice, followings);
    }

    private Slice<Member> findBySearchConditions(final MemberSearchRequest memberSearchRequest,
                                                 final Pageable pageable) {
        final CareerLevel careerLevel = parseCareerLevel(memberSearchRequest);
        final JobType jobType = parseJobType(memberSearchRequest);
        if (memberSearchRequest.getQuery() == null && careerLevel == null && jobType == null) {
            return memberRepository.findWithOutSearchConditions(pageable);
        }
        return memberRepository.findWithSearchConditions(memberSearchRequest.getQuery(), careerLevel,
                jobType, pageable);
    }

    private void setInventoryProductsToMembers(final Slice<Member> slice) {
        final List<InventoryProduct> mixedInventoryProducts = inventoryProductRepository.findWithProductByMembers(
                slice.getContent());
        for (Member member : slice.getContent()) {
            final List<InventoryProduct> memberInventoryProducts = mixedInventoryProducts.stream()
                    .filter(it -> it.getMember().isSameId(member.getId()))
                    .collect(Collectors.toList());
            member.updateInventoryProducts(memberInventoryProducts);
        }
    }

    private JobType parseJobType(final MemberSearchRequest memberSearchRequest) {
        final JobTypeConstant jobTypeConstant = memberSearchRequest.getJobType();
        if (jobTypeConstant == null) {
            return null;
        }
        return jobTypeConstant.toJobType();
    }

    private CareerLevel parseCareerLevel(final MemberSearchRequest memberSearchRequest) {
        final CareerLevelConstant careerLevelConstant = memberSearchRequest.getCareerLevel();
        if (careerLevelConstant == null) {
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
        validateMemberExists(followerId);
        validateMemberExists(followingId);
        validateNotFollowing(followerId, followingId);
        final Following following = Following.builder()
                .followerId(followerId)
                .followingId(followingId)
                .build();
        followingRepository.save(following);
        memberRepository.increaseFollowerCount(followingId);
    }

    private void validateMemberExists(final Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }
    }

    private void validateNotFollowing(final Long followerId, final Long followingId) {
        if (followingRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new AlreadyFollowingException();
        }
    }

    @Transactional
    public void unfollow(final Long followerId, final Long followingId) {
        validateMemberExists(followerId);
        validateMemberExists(followingId);
        final Following following = findFollowingRelation(followerId, followingId);
        followingRepository.delete(following);
        memberRepository.decreaseFollowerCount(followingId);
    }

    private Following findFollowingRelation(final Long followerId, final Long followingId) {
        return followingRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(NotFollowingException::new);
    }

    public MemberPageResponse findFollowingsByConditions(final Long loggedInId,
                                                         final MemberSearchRequest memberSearchRequest,
                                                         final Pageable pageable) {
        final Slice<Member> slice = findFollowingsBySearchConditions(loggedInId, memberSearchRequest, pageable);
        if (slice.isEmpty()) {
            return MemberPageResponse.ofByFollowingCondition(slice, false);
        }
        setInventoryProductsToMembers(slice);
        return MemberPageResponse.ofByFollowingCondition(slice, true);
    }

    private Slice<Member> findFollowingsBySearchConditions(final Long loggedInId,
                                                           final MemberSearchRequest memberSearchRequest,
                                                           final Pageable pageable) {
        final CareerLevel careerLevel = parseCareerLevel(memberSearchRequest);
        final JobType jobType = parseJobType(memberSearchRequest);
        if (memberSearchRequest.getQuery() == null && careerLevel == null && jobType == null) {
            return memberRepository.findFollowingsWithOutSearchConditions(loggedInId, pageable);
        }
        return memberRepository.findFollowingsWithSearchConditions(loggedInId, memberSearchRequest.getQuery(),
                careerLevel, jobType, pageable);
    }
}
