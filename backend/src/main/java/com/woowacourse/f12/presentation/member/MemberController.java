package com.woowacourse.f12.presentation.member;

import com.woowacourse.f12.application.member.MemberService;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.request.member.MemberSearchRequest;
import com.woowacourse.f12.dto.response.member.LoggedInMemberResponse;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.presentation.auth.Login;
import com.woowacourse.f12.presentation.auth.VerifiedMember;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me")
    @Login
    public ResponseEntity<LoggedInMemberResponse> showLoggedIn(@VerifiedMember final Long memberId) {
        final LoggedInMemberResponse loggedInMemberResponse = memberService.findLoggedInMember(memberId);
        return ResponseEntity.ok(loggedInMemberResponse);
    }

    @GetMapping("/{memberId}")
    @Login(required = false)
    public ResponseEntity<MemberResponse> show(@PathVariable final Long memberId,
                                               @VerifiedMember @Nullable final Long loggedInMemberId) {
        final MemberResponse memberResponse = memberService.find(memberId, loggedInMemberId);
        return ResponseEntity.ok(memberResponse);
    }

    @PatchMapping("/me")
    @Login
    public ResponseEntity<Void> updateMe(@VerifiedMember final Long memberId,
                                         @Valid @RequestBody final MemberRequest memberRequest) {
        memberService.updateMember(memberId, memberRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Login(required = false)
    public ResponseEntity<MemberPageResponse> searchMembers(@VerifiedMember @Nullable final Long loggedInId,
                                                            @ModelAttribute final MemberSearchRequest memberSearchRequest,
                                                            final Pageable pageable) {
        final MemberPageResponse memberPageResponse = memberService.findBySearchConditions(loggedInId,
                memberSearchRequest, pageable);
        return ResponseEntity.ok(memberPageResponse);
    }

    @PostMapping("/{memberId}/following")
    @Login
    public ResponseEntity<Void> follow(@VerifiedMember final Long followerId,
                                       @PathVariable("memberId") final Long followingId) {
        memberService.follow(followerId, followingId);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{memberId}/following")
    @Login
    public ResponseEntity<Void> unfollow(@VerifiedMember final Long followerId,
                                         @PathVariable("memberId") final Long followingId) {
        memberService.unfollow(followerId, followingId);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/me/followings")
    @Login
    public ResponseEntity<MemberPageResponse> searchFollowings(@VerifiedMember final Long loggedInId,
                                                               @ModelAttribute final MemberSearchRequest memberSearchRequest,
                                                               final Pageable pageable) {
        return ResponseEntity.ok(memberService.findFollowingsByConditions(loggedInId, memberSearchRequest, pageable));
    }
}
