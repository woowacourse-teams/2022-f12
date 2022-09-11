package com.woowacourse.f12.dto.response.member;

import com.woowacourse.f12.domain.member.Following;
import com.woowacourse.f12.domain.member.Member;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberPageResponse {

    private boolean hasNext;
    private List<MemberWithProfileProductResponse> items;

    private MemberPageResponse() {
    }

    private MemberPageResponse(final boolean hasNext, final List<MemberWithProfileProductResponse> items) {
        this.hasNext = hasNext;
        this.items = items;
    }

    public static MemberPageResponse ofByFollowingCondition(final Slice<Member> members, final boolean defaultFollowingCondition) {
        final List<MemberWithProfileProductResponse> memberResponses = members.getContent()
                .stream()
                .map(member -> MemberWithProfileProductResponse.of(member, defaultFollowingCondition))
                .collect(Collectors.toList());
        return new MemberPageResponse(members.hasNext(), memberResponses);
    }

    public static MemberPageResponse of(final Slice<Member> members, List<Following> followingRelations) {
        final List<MemberWithProfileProductResponse> memberResponses = members.getContent()
                .stream()
                .map(member -> MemberWithProfileProductResponse.of(member, isFollowing(followingRelations, member)))
                .collect(Collectors.toList());
        return new MemberPageResponse(members.hasNext(), memberResponses);
    }

    private static boolean isFollowing(final List<Following> followingRelations, final Member member) {
        return followingRelations.stream()
                .anyMatch(it -> it.isFollowing(member.getId()));
    }
}
