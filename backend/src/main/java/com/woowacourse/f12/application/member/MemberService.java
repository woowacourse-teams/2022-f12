package com.woowacourse.f12.application.member;


import com.woowacourse.f12.domain.member.Following;
import com.woowacourse.f12.domain.member.FollowingRepository;
import com.woowacourse.f12.domain.member.Member;
import com.woowacourse.f12.domain.member.MemberRepository;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.response.member.LoggedInMemberResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.exception.badrequest.AlreadyFollowingException;
import com.woowacourse.f12.exception.badrequest.NotFollowingException;
import com.woowacourse.f12.exception.notfound.MemberNotFoundException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
