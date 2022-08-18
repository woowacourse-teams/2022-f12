package com.woowacourse.f12.presentation.member;

import com.woowacourse.f12.application.member.MemberService;
import com.woowacourse.f12.dto.request.member.MemberRequest;
import com.woowacourse.f12.dto.request.member.MemberSearchRequest;
import com.woowacourse.f12.dto.response.member.LoggedInMemberResponse;
import com.woowacourse.f12.dto.response.member.MemberPageResponse;
import com.woowacourse.f12.dto.response.member.MemberResponse;
import com.woowacourse.f12.presentation.auth.LoginRequired;
import com.woowacourse.f12.presentation.auth.VerifiedMember;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/me")
    @LoginRequired
    public ResponseEntity<LoggedInMemberResponse> showLoggedIn(@VerifiedMember final Long memberId) {
        final LoggedInMemberResponse loggedInMemberResponse = memberService.findLoggedInMember(memberId);
        return ResponseEntity.ok(loggedInMemberResponse);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> show(@PathVariable final Long memberId, @VerifiedMember @Nullable final Long loggedInMemberId) {
        final MemberResponse memberResponse = memberService.find(memberId, loggedInMemberId);
        return ResponseEntity.ok(memberResponse);
    }

    @PatchMapping("/me")
    @LoginRequired
    public ResponseEntity<Void> updateMe(@VerifiedMember final Long memberId,
                                         @Valid @RequestBody final MemberRequest memberRequest) {
        memberService.updateMember(memberId, memberRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MemberPageResponse> searchMembers(@VerifiedMember @Nullable final Long loggedInId,
                                                            @ModelAttribute final MemberSearchRequest memberSearchRequest,
                                                            final Pageable pageable) {
        final MemberPageResponse memberPageResponse = memberService.findByContains(loggedInId, memberSearchRequest, pageable);
        return ResponseEntity.ok(memberPageResponse);
    }

    @PostMapping("/{memberId}/following")
    @LoginRequired
    public ResponseEntity<Void> follow(@VerifiedMember final Long followerId, @PathVariable("memberId") final Long followeeId) {
        memberService.follow(followerId, followeeId);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/{memberId}/following")
    @LoginRequired
    public ResponseEntity<Void> unfollow(@VerifiedMember final Long followerId, @PathVariable("memberId") final Long followeeId) {
        memberService.unfollow(followerId, followeeId);
        return ResponseEntity.noContent()
                .build();
    }
}
